package com.danielrodriguez.gotopadel.service;

import com.danielrodriguez.gotopadel.model.Inscribe;
import com.danielrodriguez.gotopadel.model.Partido;
import com.danielrodriguez.gotopadel.model.Ubicacion;
import com.danielrodriguez.gotopadel.repository.PartidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartidoService {
    private final PartidoRepository partidoRepository;
    private final UbicacionService ubicacionService;
    private final InscribeService inscribeService;

    @Autowired
    public PartidoService(PartidoRepository partidoRepository, UbicacionService ubicacionService,
            InscribeService inscribeService) {
        this.partidoRepository = partidoRepository;
        this.ubicacionService = ubicacionService;
        this.inscribeService = inscribeService;
    }

    // Método para obtener todos los partidos
    public List<Partido> obtenerTodosLosPartidos() {
        return partidoRepository.findAll();
    }

    // Método para obtener un partido por su id
    public Optional<Partido> obtenerPartidoPorId(int id) {
        return partidoRepository.findById(id);
    }

    // Método para obtener una lista de partidos por el id del usuario organizador
    public List<Partido> obtenerPartidosPorUsuario(Integer idUsuario) {
        return partidoRepository.findByUsuarioOrInscripciones_Usuario(idUsuario);
    }

    // Método para crear o actualizar un partido
    public Partido guardarPartido(Partido partido) {
        if (partido.getUbicacion() != null) {
            String nombreUbicacion = partido.getUbicacion().getNombre();
            String ciudadUbicacion = partido.getUbicacion().getCiudad();
            Ubicacion ubicacion = ubicacionService.findUbicacion(nombreUbicacion, ciudadUbicacion);
            partido.setUbicacion(ubicacion);
        }
        partidoRepository.save(partido);
        Inscribe inscribe = new Inscribe(partido.getUsuario(), partido, partido.getFechaPublicacion());
        inscribeService.inscribir(inscribe);
        return partido;

    }

    public void descontarVacante(int idPartido) {
        Optional<Partido> optionalPartido = partidoRepository.findById(idPartido);
        if (optionalPartido.isPresent()) {
            Partido partido = optionalPartido.get();
            byte vacantesActuales = partido.getVacantes();
            if (vacantesActuales > 0) {
                partido.setVacantes((byte) (vacantesActuales - 1));
                partidoRepository.save(partido);
            }
        }
    }

    public void aumentarVacante(int idPartido) {
        Optional<Partido> optionalPartido = partidoRepository.findById(idPartido);
        if (optionalPartido.isPresent()) {
            Partido partido = optionalPartido.get();
            byte vacantesActuales = partido.getVacantes();

            partido.setVacantes((byte) (vacantesActuales + 1));
            partidoRepository.save(partido);

        }
    }

    // public void descontarVacante(Partido partido) {
    // Byte vacantesActuales = partido.getVacantes();
    // if (vacantesActuales > 0) {
    // partido.setVacantes((byte) (vacantesActuales - 1));
    // partidoRepository.save(partido); // Guarda el partido actualizado en la base
    // de datos
    // }
    // }

    // Método para eliminar un partido por su id
    public void eliminarPartido(int id) {
        partidoRepository.deleteById(id);
    }

    public Integer contarPartidosPublicadosPorUsuario(Integer idUsuario) {
        return partidoRepository.countPartidosPublicadosPorUsuario(idUsuario);
    }

}
