package com.danielrodriguez.gotopadel.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.danielrodriguez.gotopadel.dto.InscripcionDTO;
import com.danielrodriguez.gotopadel.model.Inscribe;
import com.danielrodriguez.gotopadel.model.Usuario;
import com.danielrodriguez.gotopadel.repository.InscribeRepository;

@Service
public class NotificacionService {
    private final InscribeRepository inscribeRepository;

    @Autowired
    public NotificacionService(InscribeRepository inscribeRepository) {
        this.inscribeRepository = inscribeRepository;
    }

    /**
     * Actualiza el estado de la notificación para un conjunto de inscripciones
     * que no han sido notificadas aún, según el organizador del partido, y devuelve
     * la lista de usuarios que se han inscrito.
     * 
     * @param organizador El usuario organizador del partido.
     * @return Lista de usuarios que se han inscrito en los partidos del
     *         organizador.
     */
    public List<InscripcionDTO> notificarInscripciones(Usuario organizador) {
        List<Inscribe> inscripciones = inscribeRepository.findByPartido_UsuarioAndNotificadoFalse(organizador);
        List<InscripcionDTO> inscripcionesDTO = new ArrayList<>();

        for (Inscribe inscribe : inscripciones) {
            if (organizador.getIdUsuario() != inscribe.getUsuario().getIdUsuario()) {
                //inscribe.setNotificado(true);
                //inscribeRepository.save(inscribe); // Marca las inscripciones como notificadas
                inscripcionesDTO.add(new InscripcionDTO(inscribe.getUsuario(), inscribe.getPartido()));                
            }            
        }
        return inscripcionesDTO; // Devuelve la lista de DTOs con usuario y partido
    }

    public void marcarTodasComoLeidas(Usuario organizador) {
        List<Inscribe> inscripciones = inscribeRepository.findByPartido_UsuarioAndNotificadoFalse(organizador);
        for (Inscribe inscribe : inscripciones) {
            inscribe.setNotificado(true);
            inscribeRepository.save(inscribe); // Marca la inscripción como leída
        }
    }
    

}
