package com.pedidosonline.proyecto_sw_2026.service;

import com.pedidosonline.proyecto_sw_2026.infrastructure.GoogleMapsApiFake;
import com.pedidosonline.proyecto_sw_2026.infrastructure.MessageBrokerFake;
import com.pedidosonline.proyecto_sw_2026.infrastructure.KVDBFake;
import com.pedidosonline.proyecto_sw_2026.infrastructure.WebSocketServiceFake;
import com.pedidosonline.proyecto_sw_2026.model.Pedido;
import com.pedidosonline.proyecto_sw_2026.repository.BBDDPedidos;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ServicioReparto {

    private final KVDBFake redisGPS;
    private final GoogleMapsApiFake googleMaps;
    private final MessageBrokerFake broker;
    private final WebSocketServiceFake webSockets;
    private final BBDDPedidos bbddPedidos;

    public ServicioReparto(KVDBFake redisGPS, GoogleMapsApiFake googleMaps, 
                           MessageBrokerFake broker, WebSocketServiceFake webSockets, 
                           BBDDPedidos bbddPedidos) {
        this.redisGPS = redisGPS;
        this.googleMaps = googleMaps;
        this.broker = broker;
        this.webSockets = webSockets;
        this.bbddPedidos = bbddPedidos;
    }

    // CU 2: Buscar e invitar al mejor repartidor (Método interno llamado por el Broker)
    public void procesarCalculoDeRutasParaPedido(String idPedido, String coordenadasComercio) {
        List<String> candidatos = redisGPS.buscarRepartidoresCercanos(3.0);
        String matrizETAs = googleMaps.distanceMatrixRequest(candidatos, coordenadasComercio);
        
        // Suponemos que extraemos al mejor candidato tras parsear el JSON de Maps
        String mejorRepartidor = candidatos.get(0);
        
        webSockets.enrutarOferta(mejorRepartidor, 1.5, 4.50);
    }

    // CU 2: El repartidor acepta (Llamado desde RepartidorController)
    public void confirmarAceptacionOferta(String idPedido, String idRepartidor) {
        Pedido pedido = bbddPedidos.findById(idPedido)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));
        
        pedido.setEstado("REPARTIDOR_ASIGNADO");
        bbddPedidos.save(pedido);

        broker.publicarEvento("RepartidorAsignado", idPedido);
        webSockets.pushNotification(pedido.getComercio() != null ? pedido.getComercio().getIdComercio() : "Comercio", 
                                    "El repartidor " + idRepartidor + " se dirige al local.");
    }
}
