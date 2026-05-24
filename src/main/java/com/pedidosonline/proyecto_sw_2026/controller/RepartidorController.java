package com.pedidosonline.proyecto_sw_2026.controller;

import com.pedidosonline.proyecto_sw_2026.dto.RespuestaGenericaDTO;
import com.pedidosonline.proyecto_sw_2026.service.ServicioReparto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/repartidor")
public class RepartidorController {

    private final ServicioReparto servicioReparto;

    public RepartidorController(ServicioReparto servicioReparto) {
        this.servicioReparto = servicioReparto;
    }

    // ==========================================
    // CU 2: Asignación dinámica de repartidor
    // ==========================================
    @PostMapping("/aceptar-oferta")
    public ResponseEntity<RespuestaGenericaDTO> aceptarOferta(
            @RequestParam String idRepartidor, 
            @RequestParam String idPedido) {
        
        // El controlador recibe la acción del actor externo y delega la lógica de enrutamiento
        servicioReparto.confirmarAceptacionOferta(idPedido, idRepartidor);
        
        return ResponseEntity.ok(new RespuestaGenericaDTO(
                "Oferta aceptada formalmente. El repartidor está en camino al comercio.", 
                idPedido
        ));
    }
}