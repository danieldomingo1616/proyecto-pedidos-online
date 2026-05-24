package com.pedidosonline.proyecto_sw_2026.infrastructure;

import org.springframework.stereotype.Component;

@Component
public class WebSocketServiceFake {

    public void pushNotification(String destinatario, String mensaje) {
        System.out.println("[WEBSOCKET - FAKE] 🔔 Notificación enviada a [" + destinatario + "]: " + mensaje);
    }
    
    // Métodos específicos extraídos de los diagramas de secuencia
    public void enrutarOferta(String idRepartidor, double distancia, double ganancia) {
        System.out.println("[WEBSOCKET - FAKE] 🛵 Enviando oferta a repartidor " + idRepartidor + " | Distancia: " + distancia + "km | Ganancia: " + ganancia + "€");
    }
    
    public void solicitarResolucionCliente(String idCliente, String producto, double diferencia) {
        System.out.println("[WEBSOCKET - FAKE] 📱 Solicitando resolución al cliente " + idCliente + " por falta de stock de: " + producto);
    }
}