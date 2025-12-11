package com.certus.claimbook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.certus.claimbook.entities.Reclamo;
// import com.certus.claimbook.repositories.EstadoRepository;
import com.certus.claimbook.repositories.ReclamoRepository;
// import com.certus.claimbook.repositories.TipoReclamoRepository;
import java.util.List;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reclamos")
@RequiredArgsConstructor
public class ApiController {

	@Autowired
    private ReclamoRepository reclamoRepo;

    @PostMapping
    public Reclamo crear(@RequestBody Reclamo reclamo) {
        return reclamoRepo.save(reclamo);
    }

    @GetMapping
    public List<Reclamo> listar() {
        return reclamoRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reclamo> detalle(@PathVariable Long id) {
        return reclamoRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<Reclamo> editar(@PathVariable Long id, @RequestBody Reclamo data) {
        return reclamoRepo.findById(id).map(r -> {
            r.setNombre(data.getNombre());
            r.setCorreo(data.getCorreo());
            r.setTelefono(data.getTelefono());
            r.setTipo(data.getTipo());
            r.setDescripcion(data.getDescripcion());
            r.setEstado(data.getEstado());
            return ResponseEntity.ok(reclamoRepo.save(r));
        }).orElse(ResponseEntity.notFound().build());
    }
    
}
