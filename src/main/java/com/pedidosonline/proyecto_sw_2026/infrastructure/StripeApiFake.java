package com.pedidosonline.proyecto_sw_2026.infrastructure;

import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class StripeApiFake {

    public String authCharge(String tarjeta, double importe) {
        System.out.println("[STRIPE - FAKE] Pre-autorizando " + importe + "€ en la tarjeta " + tarjeta);
        // Simulamos la devolución de un token de pre-autorización
        return "token_preauth_" + UUID.randomUUID().toString();
    }

    public boolean captureCharge(String token) {
        System.out.println("[STRIPE - FAKE] Capturando cobro definitivo para el token: " + token);
        return true;
    }

    public boolean refund(String idTransaccion, double monto) {
        System.out.println("[STRIPE - FAKE] Procesando reembolso de " + monto + "€ para la transacción: " + idTransaccion);
        return true;
    }
}