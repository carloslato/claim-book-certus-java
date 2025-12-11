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
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.certus.claimbook.entities.Reclamo;
import com.certus.claimbook.repositories.EstadoRepository;
import com.certus.claimbook.entities.Estado;
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
	
	@Autowired
    private EstadoRepository estadoRepo;

    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@RequestBody Reclamo reclamo) {
    	Map<String, Object> response = new HashMap<>();
    	
    	try {
    		reclamoRepo.save(reclamo);
            
            // Respuesta exitosa
            response.put("mensaje", "Reclamo registrado exitosamente");
            //response.put("codigo_seguimiento", codigoSeguimiento);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            // Error en el registro
            response.put("mensaje", "Error al registrar el reclamo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    	
        // return reclamoRepo.save(reclamo);
    }

    @GetMapping
    public ResponseEntity<List<Reclamo>> listar() {
    	
    	try {
            List<Reclamo> reclamos = reclamoRepo.findAll();
            return ResponseEntity.ok(reclamos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    	
        //return reclamoRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reclamo> detalle(@PathVariable Long id) {
    	
    	Optional<Reclamo> reclamoOpt = reclamoRepo.findById(id);
    	
    	if (reclamoOpt.isPresent()) {
            return ResponseEntity.ok(reclamoOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    	
    	/*
        return reclamoRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        */
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
    
    @PutMapping("/estado/{id}")
    public ResponseEntity<Map<String, Object>> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, Long> datos) {
        
        Map<String, Object> response = new HashMap<>();
        Optional<Reclamo> reclamoOpt = reclamoRepo.findById(id);
        
        Estado nuevoEstado = estadoRepo.findById(datos.get("estado"))
                .orElseThrow(() -> new RuntimeException("Error en FK"));
        
        if (reclamoOpt.isPresent()) {
            Reclamo reclamo = reclamoOpt.get();
            
            // Actualizar solo el estado
            if (nuevoEstado != null) {
                reclamo.setEstado(nuevoEstado);
                reclamoRepo.save(reclamo);
                
                response.put("id", id);
                response.put("estado", nuevoEstado);
                response.put("mensaje", "Estado actualizado correctamente");
                return ResponseEntity.ok(response);
            } else {
                response.put("mensaje", "El campo 'estado' es requerido");
                return ResponseEntity.badRequest().body(response);
            }
        } else {
            response.put("mensaje", "Reclamo no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
}
