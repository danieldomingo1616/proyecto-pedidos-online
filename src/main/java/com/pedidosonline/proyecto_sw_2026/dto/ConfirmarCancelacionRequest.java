package com.pedidosonline.proyecto_sw_2026.dto;

import lombok.Data;

@Data
public class ConfirmarCancelacionRequest {
    private String idCliente;
    private String idComercio;
    private double montoReembolsoAcordado;
}
