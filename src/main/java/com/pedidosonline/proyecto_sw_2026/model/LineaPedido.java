package com.pedidosonline.proyecto_sw_2026.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "lineas_pedido")
public class LineaPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLinea;

    private int cantidad;
    private double precioUnitarioSubtotal;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto; 
}