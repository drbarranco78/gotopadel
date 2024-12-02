package com.danielrodriguez.gotopadel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.danielrodriguez.gotopadel.model.Archivo;
import com.danielrodriguez.gotopadel.model.Partido;

public interface ArchivoRepository extends JpaRepository<Archivo, Integer>{

}
