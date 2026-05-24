package com.pedidosonline.proyecto_sw_2026.dto;

import lombok.Data;

@Data
public class ResolucionIncidenciaRequest {
    private String idComercio;
    private boolean aceptaEliminarProducto;
    private double diferenciaEconomica;
}