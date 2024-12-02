package com.danielrodriguez.gotopadel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.danielrodriguez.gotopadel.model.Inscribe;
import com.danielrodriguez.gotopadel.repository.InscribeRepository;

@Service
public class InscribeService {
    
    private final InscribeRepository inscribeRepository;

    @Autowired
    public InscribeService(InscribeRepository inscribeRepository) {
        this.inscribeRepository = inscribeRepository;
    }

    public Inscribe inscribir(Inscribe inscribe){
        return inscribeRepository.save(inscribe);
    }

    public void borrarIncripcion(Integer id) {
        inscribeRepository.deleteById(id);
    }

    public List<Inscribe> obtenerListaInscritos() {
        return inscribeRepository.findAll();
    }
    public boolean verificarInscripcion(Integer idUsuario, Integer idPartido) {
        
        // Devuelve true si está inscrito, false si no lo está
        return inscribeRepository.existsByUsuario_idAndPartido_idPartido(idUsuario, idPartido);
    }

     // Nuevo método para cancelar inscripción usando usuarioId y partidoId
     public boolean cancelarInscripcionPorUsuarioYPartido(Integer idUsuario, Integer idPartido) {
        Optional<Inscribe> inscripcion = inscribeRepository.findByUsuario_idAndPartido_idPartido(idUsuario, idPartido);
        if (inscripcion.isPresent()) {
            inscribeRepository.delete(inscripcion.get());
            return true;
        }
        return false; // Retorna false si no encontró la inscripción
    }

}
