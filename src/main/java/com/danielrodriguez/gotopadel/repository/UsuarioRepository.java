package com.danielrodriguez.gotopadel.repository;

import com.danielrodriguez.gotopadel.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de la entidad Usuario que extiende JpaRepository.
 * Proporciona métodos personalizados para acceder y manipular los datos de los usuarios en la base de datos.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    /**
     * Busca un usuario por su dirección de correo electrónico.
     * 
     * @param email La dirección de correo electrónico del usuario que se busca.
     * @return El usuario con el correo electrónico proporcionado, o null si no existe.
     */
    Usuario findByEmail(String email);

    /**
     * Verifica si un usuario con el correo electrónico proporcionado ya existe en la base de datos.
     * 
     * @param email La dirección de correo electrónico del usuario a verificar.
     * @return true si el usuario con el correo electrónico existe, false en caso contrario.
     */
    boolean existsByEmail(String email);
}
