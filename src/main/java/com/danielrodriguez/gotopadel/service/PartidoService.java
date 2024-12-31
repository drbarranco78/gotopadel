package com.danielrodriguez.gotopadel.service;

import com.danielrodriguez.gotopadel.model.Inscribe;
import com.danielrodriguez.gotopadel.model.Partido;
import com.danielrodriguez.gotopadel.model.Ubicacion;
import com.danielrodriguez.gotopadel.repository.PartidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que gestiona las operaciones relacionadas con los partidos de pádel.
 */
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

    /**
     * Obtiene todos los partidos registrados en la base de datos.
     *
     * @return una lista de partidos.
     */
    public List<Partido> obtenerTodosLosPartidos() {
        return partidoRepository.findAll();
    }

    /**
     * Obtiene un partido específico por su id.
     *
     * @param id el id del partido a buscar.
     * @return un objeto Optional que contiene el partido si se encuentra, o vacío si no existe.
     */
    public Optional<Partido> obtenerPartidoPorId(int id) {
        return partidoRepository.findById(id);
    }

    /**
     * Obtiene una lista de partidos organizados o en los que un usuario está inscrito.
     *
     * @param idUsuario el id del usuario.
     * @return una lista de partidos.
     */
    public List<Partido> obtenerPartidosPorUsuario(Integer idUsuario) {
        return partidoRepository.findByUsuarioOrInscripciones_Usuario(idUsuario);
    }

    /**
     * Crea o actualiza un partido en la base de datos.
     * Si el partido tiene una ubicación asociada, se busca en la base de datos y se asigna.
     * Además, inscribe automáticamente al organizador del partido.
     *
     * @param partido el partido a guardar o actualizar.
     * @return el partido guardado o actualizado.
     */
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

    /**
     * Descuenta una vacante de un partido, si hay vacantes disponibles.
     *
     * @param idPartido el id del partido cuyo número de vacantes se desea modificar.
     */
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

    /**
     * Aumenta el número de vacantes de un partido.
     *
     * @param idPartido el id del partido cuyo número de vacantes se desea incrementar.
     */
    public void aumentarVacante(int idPartido) {
        Optional<Partido> optionalPartido = partidoRepository.findById(idPartido);
        if (optionalPartido.isPresent()) {
            Partido partido = optionalPartido.get();
            byte vacantesActuales = partido.getVacantes();
            partido.setVacantes((byte) (vacantesActuales + 1));
            partidoRepository.save(partido);
        }
    }

    /**
     * Elimina un partido de la base de datos por su id.
     *
     * @param id el id del partido a eliminar.
     */
    public void eliminarPartido(int id) {
        partidoRepository.deleteById(id);
    }

    /**
     * Cuenta el número de partidos publicados por un usuario.
     *
     * @param idUsuario el id del usuario para contar sus partidos publicados.
     * @return el número de partidos publicados por el usuario.
     */
    public Integer contarPartidosPublicadosPorUsuario(Integer idUsuario) {
        return partidoRepository.countPartidosPublicadosPorUsuario(idUsuario);
    }
}
