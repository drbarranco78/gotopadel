package com.danielrodriguez.gotopadel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danielrodriguez.gotopadel.model.Inscribe;
import com.danielrodriguez.gotopadel.service.InscribeService;

@RestController
@RequestMapping("/api/inscripciones")

public class InscribeController {

     private final InscribeService inscribeService;

    @Autowired
    public InscribeController(InscribeService inscribeService) {
        this.inscribeService = inscribeService;
    }

    @PostMapping
    public Inscribe createInscription(@RequestBody Inscribe inscribe) {
        return inscribeService.inscribir(inscribe);
    }

    @DeleteMapping("/{id}")
    public void deleteInscription(@PathVariable Integer id) {
        inscribeService.borrarIncripcion(id);
    }

    @GetMapping
    public List<Inscribe> getAllInscriptions() {
        return inscribeService.obtenerListaInscritos();
    }

    



}
