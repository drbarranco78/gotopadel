package com.danielrodriguez.gotopadel.service;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.danielrodriguez.gotopadel.model.Archivo;
import com.danielrodriguez.gotopadel.model.Partido;
import com.danielrodriguez.gotopadel.repository.ArchivoRepository;
import com.danielrodriguez.gotopadel.repository.PartidoRepository;

import jakarta.transaction.Transactional;

/**
 * Servicio encargado de archivar partidos, gestionando la transferencia de datos entre la tabla
 * PARTIDO y la tabla ARCHIVO, así como la recuperación de partidos archivados.
 */
@Service
public class ArchivoService {
    
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final ArchivoRepository archivoRepository;
    private final PartidoRepository partidoRepository;

    @Autowired
    public ArchivoService(ArchivoRepository archivoRepository, PartidoRepository partidoRepository) {
        this.archivoRepository = archivoRepository;
        this.partidoRepository = partidoRepository;
    }

    /**
     * Archiva un partido moviéndolo de la tabla PARTIDO a la tabla ARCHIVO.
     * 
     * @param idPartido el id del partido a archivar.
     * @param motivoArchivado el motivo por el cual el partido está siendo archivado.
     *                         Se usa un valor de 1 para "Partido Jugado" y otro valor para "Partido Cancelado".
     */
    @Transactional
    public void archivarPartido(Integer idPartido, Integer motivoArchivado) {
        // Buscar el partido por id
        Partido partido = partidoRepository.findById(idPartido).orElseThrow(() -> new IllegalArgumentException("Partido no encontrado"));
        
        // Determinar el motivo del archivado basado en el valor recibido
        String motivo = motivoArchivado == 1 ? "Partido Jugado" : "Partido Cancelado";
        
        // Crear el objeto Archivo con la información del partido y el motivo de archivado
        Archivo archivo = new Archivo();
        archivo.setIdPartido(partido.getIdPartido());
        archivo.setIdUsuario(partido.getUsuario().getIdUsuario());
        archivo.setTipoPartido(partido.getTipoPartido());
        archivo.setNivel(partido.getNivel());
        archivo.setFechaPartido(partido.getFechaPartido());
        archivo.setFechaPublicacion(partido.getFechaPublicacion());
        archivo.setHoraPartido(partido.getHoraPartido());
        archivo.setUbicacion(partido.getUbicacion().getIdUbicacion());
        archivo.setComentarios(partido.getComentarios());
        archivo.setFechaArchivo(java.time.LocalDate.now().format(formatter).toString());  // Fecha de archivado
        archivo.setMotivoArchivado(motivo);

        // Guardar el partido archivado en la tabla ARCHIVO
        archivoRepository.save(archivo);

        // Eliminar el partido de la tabla PARTIDO
        partidoRepository.deleteById(idPartido);
    }

    /**
     * Obtiene una lista de todos los partidos archivados.
     *
     * @return una lista de partidos archivados.
     */
    public List<Archivo> obtenerPartidosArchivados() {
        return archivoRepository.findAll();
    }

    /**
     * Elimina un partido archivado por su id.
     *
     * @param id el id del partido archivado a eliminar.
     */
    public void eliminarPartidoArchivadoPorId(Integer id) {
        archivoRepository.deleteById(id);
    }
}
