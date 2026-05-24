package com.pedidosonline.proyecto_sw_2026.infrastructure;

import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;

@Component
public class KVDBFake {

    public List<String> buscarRepartidoresCercanos(double radioKm) {
        System.out.println("[REDIS GPS - FAKE] Buscando repartidores conectados en un radio de " + radioKm + " km...");
        // Simulamos que encuentra un par de repartidores en la zona
        return Arrays.asList("REP-001", "REP-089", "REP-104");
    }
    
    public void actualizarUbicacion(String idRepartidor, String lat, String lon) {
        System.out.println("[REDIS GPS - FAKE] Ubicación de " + idRepartidor + " actualizada a [" + lat + ", " + lon + "]");
    }
}
