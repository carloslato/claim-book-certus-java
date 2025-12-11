package com.certus.claimbook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.certus.claimbook.entities.Reclamo;
import com.certus.claimbook.entities.Respuesta;
import com.certus.claimbook.repositories.EstadoRepository;
import com.certus.claimbook.entities.Estado;
// import com.certus.claimbook.repositories.EstadoRepository;
import com.certus.claimbook.repositories.ReclamoRepository;
import com.certus.claimbook.repositories.RespuestaRepository;
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
	
	@Autowired
    private RespuestaRepository respuestaRepo;

    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@RequestBody Reclamo reclamo) {
    	Map<String, Object> response = new HashMap<>();
    	
    	try {
    		
    		// Generar código de seguimiento único
            String codigoSeguimiento = generarCodigoSeguimiento();
            reclamo.setCodigoSeguimiento(codigoSeguimiento);
    		
    		reclamoRepo.save(reclamo);
    		
    		Long id = reclamo.getId(); 
            
            // Respuesta exitosa
            response.put("mensaje", "Reclamo registrado exitosamente");
            response.put("codigo_seguimiento", codigoSeguimiento);
            response.put("id_reclamo", id);
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
    
    @PostMapping("/responder/{id}")
    public ResponseEntity<?> responder(
            @PathVariable Long id,
            @RequestBody Respuesta dto
    ) {
        Reclamo reclamo = reclamoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Reclamo no encontrado"));

        Respuesta r = new Respuesta();
        r.setReclamo(reclamo);
        r.setMensaje(dto.getMensaje());
        r.setAutor(dto.getAutor()); // USUARIO o EMPLEADO

        respuestaRepo.save(r);

        return ResponseEntity.ok("Respuesta agregada");
    }

    
    /**
     * ENDPOINT 4: Buscar reclamo por código de seguimiento
     * Método: GET
     * Ruta: /api/reclamos/buscar?codigo={codigo}
     * Parámetro: codigo (String) - Código de seguimiento
     * Consumido por: consulta-estado.html
     * 
     * Response (JSON): Igual que el endpoint 3
     */
    @GetMapping("/buscar")
    public ResponseEntity<Reclamo> buscarPorCodigo(@RequestParam String codigo) {
        Optional<Reclamo> reclamoOpt = reclamoRepo.findByCodigoSeguimiento(codigo.toUpperCase());
        
        if (reclamoOpt.isPresent()) {
            return ResponseEntity.ok(reclamoOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    /**
     * Método auxiliar: Generar código de seguimiento único
     * Formato: REQ-YYYY-NNN (Ej: REQ-2025-001)
     */
    private String generarCodigoSeguimiento() {
        // Obtener el año actual
        String anio = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy"));
        
        // Contar reclamos del año actual para generar el número consecutivo
        long count = reclamoRepo.countByCodigoSeguimientoStartingWith("REQ-" + anio);
        
        // Generar código con formato REQ-YYYY-NNN
        return String.format("REQ-%s-%03d", anio, count + 1);
    }
}
