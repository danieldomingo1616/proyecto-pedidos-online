package com.pedidosonline.proyecto_sw_2026.dto;

import lombok.Data;
import java.util.List;

@Data
public class CrearPedidoRequest {
    private String idCliente;
    private String idComercio;
    private String metodoPago; // ej: "tarjeta_credito"
    private List<ItemCarritoDto> carrito;
    private double montoTotalSimulado;
    @Data
    public static class ItemCarritoDto {
        private String idProducto;
        private int cantidad;
    }
}