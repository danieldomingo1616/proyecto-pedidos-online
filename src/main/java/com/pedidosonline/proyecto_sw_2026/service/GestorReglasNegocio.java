package com.pedidosonline.proyecto_sw_2026.service;

import org.springframework.stereotype.Service;

@Service
public class GestorReglasNegocio {

    /**
     * Devuelve el porcentaje del monto que se le reembolsará al cliente.
     */
    public double evaluarPoliticaCancelacion(String estadoActual) {
        if (estadoActual == null) return 0.0;
        
        return switch (estadoActual.toUpperCase()) {
            case "PENDIENTE_CONFIRMACION" -> 1.0; // Reembolso íntegro
            case "EN_PREPARACION" -> 0.5;         // Reembolso del 50%
            case "REPARTIDOR_ASIGNADO", "EN_CAMINO" -> 0.0; // Sin reembolso
            default -> 0.0;
        };
    }
}
