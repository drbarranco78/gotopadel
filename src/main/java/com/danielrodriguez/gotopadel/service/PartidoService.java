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
 * Proporciona métodos para crear, actualizar, eliminar y consultar partidos en la base de datos.
 */
@Service
public class PartidoService {

    private final PartidoRepository partidoRepository;
    private UbicacionService ubicacionService;
    private InscribeService inscribeService;

    /**
     * Constructor que inyecta las dependencias necesarias para la gestión de partidos.
     *
     * @param partidoRepository el repositorio para acceder y manipular datos de partidos
     * @param ubicacionService el servicio para gestionar datos de ubicaciones
     * @param inscribeService el servicio para gestionar inscripciones en partidos
     */
    @Autowired
    public PartidoService(PartidoRepository partidoRepository, UbicacionService ubicacionService,
                          InscribeService inscribeService) {
        this.partidoRepository = partidoRepository;
        this.ubicacionService = ubicacionService;
        this.inscribeService = inscribeService;
    }

    /**
     * Constructor alternativo que inyecta solo el repositorio de partidos.
     *
     * @param partidoRepository el repositorio para acceder y manipular datos de partidos
     */
    public PartidoService(PartidoRepository partidoRepository) {
        this.partidoRepository = partidoRepository;
    }

    /**
     * Obtiene todos los partidos registrados en la base de datos.
     *
     * @return una lista de todos los partidos registrados
     */
    public List<Partido> obtenerTodosLosPartidos() {
        return partidoRepository.findAll();
    }

    /**
     * Obtiene un partido específico por su identificador.
     *
     * @param id el identificador del partido a buscar
     * @return un {@code Optional<Partido>} que contiene el partido si se encuentra, o vacío si no existe
     */
    public Optional<Partido> obtenerPartidoPorId(int id) {
        return partidoRepository.findById(id);
    }

    /**
     * Obtiene una lista de partidos organizados o en los que un usuario está inscrito.
     *
     * @param idUsuario el identificador del usuario
     * @return una lista de partidos relacionados con el usuario
     */
    public List<Partido> obtenerPartidosPorUsuario(Integer idUsuario) {
        return partidoRepository.findByUsuarioOrInscripciones_Usuario(idUsuario);
    }

    /**
     * Crea o actualiza un partido en la base de datos.
     * Si el partido tiene una ubicación asociada, la busca y asigna. Además, inscribe automáticamente
     * al organizador del partido.
     *
     * @param partido el partido a guardar o actualizar
     * @return el partido guardado o actualizado
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
     * Reduce en uno el número de vacantes disponibles en un partido, si hay vacantes.
     *
     * @param idPartido el identificador del partido cuya cantidad de vacantes se desea modificar
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
     * Aumenta en uno el número de vacantes disponibles en un partido.
     *
     * @param idPartido el identificador del partido cuya cantidad de vacantes se desea incrementar
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
     * Elimina un partido de la base de datos por su identificador.
     *
     * @param id el identificador del partido a eliminar
     * @return {@code true} si el partido existía y fue eliminado, {@code false} si no existía
     */
    public boolean eliminarPartido(int id) {
        if (partidoRepository.existsById(id)) {
            partidoRepository.deleteById(id);
            return true; // Indica que el partido existía y fue eliminado
        }
        return false; // Indica que el partido no existía
    }

    /**
     * Cuenta el número de partidos publicados por un usuario.
     *
     * @param idUsuario el identificador del usuario cuyos partidos publicados se desean contar
     * @return el número total de partidos publicados por el usuario
     */
    public Integer contarPartidosPublicadosPorUsuario(Integer idUsuario) {
        return partidoRepository.countPartidosPublicadosPorUsuario(idUsuario);
    }
}