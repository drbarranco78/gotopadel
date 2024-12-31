package com.danielrodriguez.gotopadel.repository;

import com.danielrodriguez.gotopadel.model.Credenciales;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la entidad {@link Credenciales}.
 * Proporciona operaciones CRUD para gestionar las credenciales de los usuarios.
 * Extiende de JpaRepository, por lo que hereda los métodos estándar de acceso a datos.
 */
public interface CredencialesRepository extends JpaRepository<Credenciales, Integer> {

}
