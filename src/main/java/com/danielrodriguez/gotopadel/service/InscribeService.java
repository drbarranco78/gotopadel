package com.danielrodriguez.gotopadel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.danielrodriguez.gotopadel.model.Inscribe;
import com.danielrodriguez.gotopadel.repository.InscribeRepository;

/**
 * Servicio encargado de gestionar las inscripciones de los usuarios a los
 * partidos.
 */
@Service
public class InscribeService {

    private final InscribeRepository inscribeRepository;

    /**
     * Constructor que inyecta el repositorio de inscripciones.
     *
     * @param inscribeRepository el repositorio para acceder y manipular datos relacionados con las inscripciones
     */
    @Autowired
    public InscribeService(InscribeRepository inscribeRepository) {
        this.inscribeRepository = inscribeRepository;
    }

    /**
     * Inscribe a un usuario en un partido.
     *
     * @param inscribe objeto que contiene los datos de la inscripción.
     * @return el objeto Inscribe guardado en la base de datos.
     */
    public Inscribe inscribir(Inscribe inscribe) {
        boolean yaInscrito = inscribeRepository.existsByUsuario_idAndPartido_idPartido(
                inscribe.getUsuario().getIdUsuario(),
                inscribe.getPartido().getIdPartido());

        if (yaInscrito) {
            return null; // Ya está inscrito, devuelve -1
        }
        // Inscripción exitosa        
        return inscribeRepository.save(inscribe);
    }  

    /**
     * Elimina la inscripción de un usuario a un partido por el id de la
     * inscripción.
     *
     * @param id el id de la inscripción que se desea borrar.
     */
    public void borrarIncripcion(Integer id) {
        inscribeRepository.deleteById(id);
    }

    /**
     * Obtiene una lista de todas las inscripciones de usuarios a partidos.
     *
     * @return una lista de inscripciones.
     */
    public List<Inscribe> obtenerListaInscritos() {
        return inscribeRepository.findAll();
    }

    /**
     * Obtiene una lista de inscripciones para un partido específico.
     *
     * @param idPartido ID del partido.
     * @return una lista de inscripciones para ese partido.
     */
    public List<Inscribe> obtenerInscripcionesPorPartido(Integer idPartido) {
        return inscribeRepository.findByPartidoIdPartido(idPartido);
    }

    /**
     * Verifica si un usuario está inscrito en un partido específico.
     *
     * @param idUsuario el id del usuario a verificar.
     * @param idPartido el id del partido a verificar.
     * @return true si el usuario está inscrito, false si no lo está.
     */
    public boolean verificarInscripcion(Integer idUsuario, Integer idPartido) {
        // Devuelve true si está inscrito, false si no lo está
        return inscribeRepository.existsByUsuario_idAndPartido_idPartido(idUsuario, idPartido);
    }

    /**
     * Cancela la inscripción de un usuario a un partido específico.
     *
     * @param idUsuario el id del usuario.
     * @param idPartido el id del partido.
     * @return true si la inscripción fue cancelada, false si no se encontró la
     *         inscripción.
     */
    public boolean cancelarInscripcionPorUsuarioYPartido(Integer idUsuario, Integer idPartido) {
        Optional<Inscribe> inscripcion = inscribeRepository.findByUsuario_idAndPartido_idPartido(idUsuario, idPartido);
        if (inscripcion.isPresent()) {
            inscribeRepository.delete(inscripcion.get());
            return true;
        }
        return false; // Retorna false si no encontró la inscripción
    }

    /**
     * Obtiene la cantidad de partidos en los que un usuario está inscrito.
     *
     * @param idUsuario el ID del usuario.
     * @return el número de partidos en los que el usuario está inscrito.
     */
    public int obtenerCantidadInscripciones(int idUsuario) {
        return inscribeRepository.countByUsuario_id(idUsuario);
    }
}
