package com.danielrodriguez.gotopadel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.danielrodriguez.gotopadel.model.Inscribe;

public interface InscribeRepository extends JpaRepository<Inscribe,Integer>{
    boolean existsByUsuario_idAndPartido_idPartido(Integer usuarioId, Integer partidoId);

    // Busca el ID de inscripcion a partir del ID de usuario y el ID de partido
    Optional<Inscribe> findByUsuario_idAndPartido_idPartido(Integer usuarioId, Integer partidoId);


}
