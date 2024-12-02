package com.danielrodriguez.gotopadel.repository;

import com.danielrodriguez.gotopadel.model.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {

    @Query("SELECT u FROM Ubicacion u WHERE u.nombre = :nombre AND u.ciudad = :ciudad")
    Optional<Ubicacion> findUbicacion(@Param("nombre") String nombre, @Param("ciudad") String ciudad);
}
