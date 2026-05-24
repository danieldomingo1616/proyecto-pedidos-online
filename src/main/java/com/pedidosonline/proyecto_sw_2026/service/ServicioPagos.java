package com.pedidosonline.proyecto_sw_2026.service;

import com.pedidosonline.proyecto_sw_2026.infrastructure.StripeApiFake;
import org.springframework.stereotype.Service;

@Service
public class ServicioPagos {

    private final StripeApiFake stripeApi;

    public ServicioPagos(StripeApiFake stripeApi) {
        this.stripeApi = stripeApi;
    }

    public String solicitarPreAutorizacion(double importe, String metodoPago) {
        // En un caso real aquí verificaríamos la pasarela
        return stripeApi.authCharge(metodoPago, importe);
    }

    public boolean capturarFondos(String tokenPreauth) {
        return stripeApi.captureCharge(tokenPreauth);
    }

    public boolean procesarReembolso(String idTransaccion, double monto) {
        return stripeApi.refund(idTransaccion, monto);
    }
}
