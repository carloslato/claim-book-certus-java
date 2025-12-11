package com.certus.claimbook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.certus.claimbook.entities.Estado;
import com.certus.claimbook.entities.Reclamo;
import com.certus.claimbook.repositories.EstadoRepository;
import com.certus.claimbook.repositories.ReclamoRepository;
import com.certus.claimbook.repositories.TipoReclamoRepository;

@Controller
@RequestMapping("/legacy")
public class ReclamoController {

    @Autowired
    private ReclamoRepository reclamoRepo;

    @Autowired
    private TipoReclamoRepository tipoRepo;
    
    @Autowired
    private EstadoRepository estadoRepo;

    @GetMapping("/nuevo")
    public String formulario(Model model) {
        model.addAttribute("reclamo", new Reclamo());
        model.addAttribute("tipos", tipoRepo.findAll());
        return "formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Reclamo r) {
    	
        Estado estadoPorDefecto = estadoRepo.findById(1L)
                                            .orElseThrow(() -> new RuntimeException("Error en FK"));
        
        r.setEstado(estadoPorDefecto);
    	
        reclamoRepo.save(r);
        return "redirect:/reclamos/lista";
    }

    @GetMapping("/lista")
    public String lista(Model model) {
        model.addAttribute("reclamos", reclamoRepo.findAll());
        return "lista";
    }
    
    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        var reclamo = reclamoRepo.findById(id).orElse(null);
        if (reclamo == null) return "redirect:/reclamos/lista";

        model.addAttribute("reclamo", reclamo);
        return "detalle";
    }

}
