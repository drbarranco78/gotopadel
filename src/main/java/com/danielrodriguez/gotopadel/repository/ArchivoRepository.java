package com.danielrodriguez.gotopadel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.danielrodriguez.gotopadel.model.Archivo;

/**
 * Repositorio para la entidad Archivo.
 * Proporciona operaciones CRUD para gestionar los archivos de partidos archivados.
 * Extiende de JpaRepository, lo que permite realizar operaciones básicas de acceso a datos.
 */
public interface ArchivoRepository extends JpaRepository<Archivo, Integer> {

}
