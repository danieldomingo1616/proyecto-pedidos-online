package com.pedidosonline.proyecto_sw_2026.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "productos")
public class Producto {
    @Id
    private String idProducto;

    private String nombre;
    private String descripcion;
    private double precio;
    private boolean disponibilidadStock;

    @ManyToOne
    @JoinColumn(name = "comercio_id")
    private Comercio comercio; 
}