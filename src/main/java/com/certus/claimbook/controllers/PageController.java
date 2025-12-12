package com.certus.claimbook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

//import com.certus.claimbook.entities.Estado;
import com.certus.claimbook.entities.Reclamo;
import com.certus.claimbook.repositories.EstadoRepository;
import com.certus.claimbook.repositories.ReclamoRepository;
import com.certus.claimbook.repositories.TipoReclamoRepository;

@Controller
@RequestMapping("/reclamos")
public class PageController {

    @Autowired
    private ReclamoRepository reclamoRepo;

    @Autowired
    private EstadoRepository estadoRepo;
    
    @Autowired
    private TipoReclamoRepository tipoRepo; 

    // --- USUARIOS ---

    @GetMapping("/enviar")
    public String enviarReclamoForm(Model model) {
        model.addAttribute("reclamo", new Reclamo());
        model.addAttribute("tipos", tipoRepo.findAll());      
        model.addAttribute("estados", estadoRepo.findAll()); 
        return "usuario/enviar-reclamo";
    }

    @GetMapping("/consultar")
    public String consultarReclamoForm() {
        return "usuario/consultar-reclamo";
    }

    @GetMapping("/detalle/{codigo}")
    public String detalleReclamo(@PathVariable String codigo, Model model) {

        Reclamo r = reclamoRepo.findByCodigoSeguimiento(codigo.toUpperCase())
                .orElse(null);

        model.addAttribute("reclamo", r);
        return "usuario/detalle-reclamo";
    }

    @GetMapping("/gracias")
    public String paginaGracias() {
        return "usuario/gracias";
    }

    // --- GESTIÓN / ADMIN ---

    @GetMapping("/admin/listar")
    public String adminListar(Model model) {
        model.addAttribute("reclamos", reclamoRepo.findAll());
        return "admin/listar";
    }

    @GetMapping("/admin/detalle/{id}")
    public String adminDetalle(@PathVariable Long id, Model model) {
        model.addAttribute("reclamo", reclamoRepo.findById(id).orElse(null));
        return "admin/detalle";
    }

    @GetMapping("/admin/buscar")
    public String adminBuscar() {
        return "admin/buscar";
    }

    
    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {

        long total = reclamoRepo.count();

        long abiertos = reclamoRepo.countByEstadoDescripcion("Pendiente");
        long proceso = reclamoRepo.countByEstadoDescripcion("En revisión");
        long cerrados = reclamoRepo.countByEstadoDescripcion("Cerrado");

        model.addAttribute("total", total);
        model.addAttribute("abiertos", abiertos);
        model.addAttribute("proceso", proceso);
        model.addAttribute("cerrados", cerrados);

        return "admin/dashboard";
    }
    
}

