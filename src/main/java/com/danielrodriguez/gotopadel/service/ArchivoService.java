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

    @Transactional
    public void archivarPartido(Integer idPartido, Integer motivoArchivado) {
        // Buscar el partido por id
        Partido partido = partidoRepository.findById(idPartido).orElseThrow(() -> new IllegalArgumentException("Partido no encontrado"));
        String motivo = motivoArchivado==1? "Partido Jugado" : "Partido Cancelado";
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

        // Guardar el partido en la tabla ARCHIVO
        archivoRepository.save(archivo);

        // Eliminar el partido de la tabla PARTIDO
        partidoRepository.deleteById(idPartido);
    }

    public List<Archivo> obtenerPartidosArchivados() {
        return archivoRepository.findAll();
    }

    public void eliminarPartidoArchivadoPorId(Integer id){
        archivoRepository.deleteById(id);
    }
}
