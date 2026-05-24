package com.pedidosonline.proyecto_sw_2026.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "incidencias_stock")
public class IncidenciaStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idIncidencia;

    private String descripcion;
    private String solucionAplicada;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto productoAfectado;
}