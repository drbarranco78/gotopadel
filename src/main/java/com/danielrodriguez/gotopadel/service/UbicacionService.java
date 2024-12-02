package com.danielrodriguez.gotopadel.service;

import com.danielrodriguez.gotopadel.model.Ubicacion;
import com.danielrodriguez.gotopadel.repository.UbicacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UbicacionService {

    @Autowired
    private UbicacionRepository ubicacionRepository;

    // Buscar la ubicación por nombre y ciudad
    public Ubicacion findUbicacion(String nombre, String ciudad) {
        return ubicacionRepository.findUbicacion(nombre, ciudad).orElse(null);
    }

    // Crear una nueva ubicación si no existe
    public Ubicacion createUbicacion(String nombre, String ciudad) {
        Ubicacion nuevaUbicacion = new Ubicacion();
        nuevaUbicacion.setNombre(nombre);
        nuevaUbicacion.setCiudad(ciudad);
        return ubicacionRepository.save(nuevaUbicacion); // Guardar en la BD
    }
}
