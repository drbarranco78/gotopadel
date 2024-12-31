package com.danielrodriguez.gotopadel.service;

import com.danielrodriguez.gotopadel.model.Ubicacion;
import com.danielrodriguez.gotopadel.repository.UbicacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio que gestiona las operaciones relacionadas con las ubicaciones.
 */
@Service
public class UbicacionService {

    @Autowired
    private UbicacionRepository ubicacionRepository;

    /**
     * Busca una ubicación por su nombre y ciudad.
     *
     * @param nombre el nombre de la ubicación.
     * @param ciudad la ciudad donde se encuentra la ubicación.
     * @return el objeto Ubicacion si se encuentra en la base de datos; de lo contrario, retorna null.
     */
    public Ubicacion findUbicacion(String nombre, String ciudad) {
        return ubicacionRepository.findUbicacion(nombre, ciudad).orElse(null);
    }

    /**
     * Crea una nueva ubicación en la base de datos si no existe.
     *
     * @param nombre el nombre de la nueva ubicación.
     * @param ciudad la ciudad donde se encuentra la nueva ubicación.
     * @return el objeto Ubicacion creado y guardado en la base de datos.
     */
    public Ubicacion createUbicacion(String nombre, String ciudad) {
        Ubicacion nuevaUbicacion = new Ubicacion();
        nuevaUbicacion.setNombre(nombre);
        nuevaUbicacion.setCiudad(ciudad);
        return ubicacionRepository.save(nuevaUbicacion); // Guardar en la BD
    }
}

