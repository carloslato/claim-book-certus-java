package com.certus.claimbook.entities;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Reclamo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String correo;
    private String telefono;

    @ManyToOne
    @JoinColumn(name = "id_tipo")
    private TipoReclamo tipo;

    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "id_estado")
    private Estado estado;

    private LocalDateTime fechaCreacion = LocalDateTime.now();
}