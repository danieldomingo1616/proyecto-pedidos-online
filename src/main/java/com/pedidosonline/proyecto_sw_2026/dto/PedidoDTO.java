package com.pedidosonline.proyecto_sw_2026.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoDTO {
    private String idPedido;
    private String estado;
    private double montoTotal;
    private int tiempoEstimadoEntrega;
    private LocalDateTime fechaHora;
    
    // Solo devolvemos datos básicos anidados para no sobrecargar
    private String nombreComercio;
    private String nombreRepartidor;
    private List<LineaPedidoDTO> lineas;
}