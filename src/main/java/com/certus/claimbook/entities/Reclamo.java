package com.certus.claimbook.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Reclamo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String correo;
    private String telefono;
    @Column(name = "codigo_seguimiento", unique = true, nullable = true)
    private String codigoSeguimiento;

    @ManyToOne
    @JoinColumn(name = "id_tipo")
    private TipoReclamo tipo;

    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "id_estado")
    private Estado estado;
    
    @OneToMany(mappedBy = "reclamo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Respuesta> respuestas = new ArrayList<>();

    private LocalDateTime fechaCreacion = LocalDateTime.now();
}