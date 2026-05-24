package com.pedidosonline.proyecto_sw_2026.dto;

import lombok.Data;

@Data
public class LineaPedidoDTO {
    private String nombreProducto;
    private int cantidad;
    private double precioUnitarioSubtotal;
}