package com.pedidosonline.proyecto_sw_2026.infrastructure;

import org.springframework.stereotype.Component;

@Component
public class MessageBrokerFake {

    public void publicarEvento(String tipoEvento, String payload) {
        System.out.println("[MESSAGE BROKER - FAKE] Evento publicado -> Tipo: " + tipoEvento + " | Datos: " + payload);
    }
}