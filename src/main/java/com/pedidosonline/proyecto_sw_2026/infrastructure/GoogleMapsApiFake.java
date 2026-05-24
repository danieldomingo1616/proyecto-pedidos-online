package com.pedidosonline.proyecto_sw_2026.infrastructure;

import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class GoogleMapsApiFake {

    public String distanceMatrixRequest(List<String> listaRepartidores, String origen) {
        System.out.println("[GOOGLE MAPS - FAKE] Calculando matriz de distancias desde " + origen + " para " + listaRepartidores.size() + " repartidores.");
        // Simulamos que devuelve un JSON o un objeto con las distancias y ETAs
        return "{ \"status\": \"OK\", \"repartidorOptimo\": \"" + listaRepartidores.get(0) + "\", \"etaMinutos\": 5 }";
    }
}
