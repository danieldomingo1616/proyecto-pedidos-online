package com.pedidosonline.proyecto_sw_2026.dto;

import lombok.Data;

@Data
public class RespuestaGenericaDTO {
    private String mensaje;
    private String idReferencia; // Puede ser el id del pedido afectado
    
    public RespuestaGenericaDTO(String mensaje, String idReferencia) {
        this.mensaje = mensaje;
        this.idReferencia = idReferencia;
    }
}