package com.danielrodriguez.gotopadel.repository;

import com.danielrodriguez.gotopadel.model.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

/**
 * Repositorio de la entidad Ubicacion que extiende JpaRepository.
 * Proporciona métodos personalizados para acceder y manipular los datos de ubicaciones en la base de datos.
 */
public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {

    /**
     * Consulta para obtener una ubicación específica basada en su nombre y ciudad.
     * Si existen varias ubicaciones con el mismo nombre o ciudad, se devuelve la primera coincidencia.
     *
     * @param nombre El nombre de la ubicación que se va a buscar.
     * @param ciudad El nombre de la ciudad donde se encuentra la ubicación.
     * @return Un objeto Optional que contiene la ubicación si se encuentra, o está vacío si no hay coincidencias.
     */
    @Query("SELECT u FROM Ubicacion u WHERE u.nombre = :nombre AND u.ciudad = :ciudad")
    Optional<Ubicacion> findUbicacion(@Param("nombre") String nombre, @Param("ciudad") String ciudad);
}
