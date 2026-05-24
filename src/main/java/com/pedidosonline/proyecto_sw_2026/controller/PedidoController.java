package com.pedidosonline.proyecto_sw_2026.controller;

import com.pedidosonline.proyecto_sw_2026.dto.ConfirmarCancelacionRequest;
import com.pedidosonline.proyecto_sw_2026.dto.CrearPedidoRequest;
import com.pedidosonline.proyecto_sw_2026.dto.ResolucionIncidenciaRequest;
import com.pedidosonline.proyecto_sw_2026.dto.RespuestaGenericaDTO;
import com.pedidosonline.proyecto_sw_2026.service.ServicioPedidos;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pedido")
public class PedidoController {

    // Inyección de dependencias obligatoria por constructor (Sección 4.2.3 del tutorial)
    private final ServicioPedidos servicioPedidos;

    public PedidoController(ServicioPedidos servicioPedidos) {
        this.servicioPedidos = servicioPedidos;
    }

    // ==========================================
    // CU 1: Procesar y confirmar pedido
    // ==========================================
    @PostMapping
    public ResponseEntity<RespuestaGenericaDTO> crearPedido(@RequestBody CrearPedidoRequest request) {
        // Recibe el DTO de entrada, delega en el servicio experto y responde con un DTO limpio
        String idPedido = servicioPedidos.procesarNuevaCompra(
                request.getMontoTotalSimulado(), // extraído de la lógica de negocio
                request.getMetodoPago(), 
                request.getIdComercio(),
                request.getIdCliente()
        );
        return ResponseEntity.ok(new RespuestaGenericaDTO("Pedido registrado, esperando confirmación del comercio", idPedido));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<RespuestaGenericaDTO> confirmarPedido(
            @PathVariable String id, 
            @RequestParam String tokenPreauth, 
            @RequestParam String idCliente) {
        
        servicioPedidos.confirmarPreparacion(id, tokenPreauth, idCliente);
        return ResponseEntity.ok(new RespuestaGenericaDTO("El comercio ha aceptado el pedido. En preparación.", id));
    }

    // ==========================================
    // CU 3: Gestión de Incidencia por Stock
    // ==========================================
    @PutMapping("/{id}/incidencia")
    public ResponseEntity<RespuestaGenericaDTO> registrarIncidencia(
            @PathVariable String id, 
            @RequestParam String idProducto, 
            @RequestParam String idCliente) {
        
        servicioPedidos.registrarIncidenciaStock(id, idProducto, idCliente);
        return ResponseEntity.ok(new RespuestaGenericaDTO("Incidencia por falta de stock registrada. Alerta enviada al cliente.", id));
    }

    @PostMapping("/{id}/resolucion")
    public ResponseEntity<RespuestaGenericaDTO> resolverIncidencia(
            @PathVariable String id, 
            @RequestBody ResolucionIncidenciaRequest request) {
        
        if (request.isAceptaEliminarProducto()) {
            servicioPedidos.aplicarResolucionEliminar(id, request.getDiferenciaEconomica(), request.getIdComercio());
            return ResponseEntity.ok(new RespuestaGenericaDTO("Resolución aplicada: Producto eliminado y reembolso parcial emitido.", id));
        }
        return ResponseEntity.badRequest().body(new RespuestaGenericaDTO("Resolución no soportada en este endpoint", id));
    }

    // ==========================================
    // CU 4: Cancelación de Pedido
    // ==========================================
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<RespuestaGenericaDTO> evaluarCancelacion(
            @PathVariable String id, 
            @RequestParam String idCliente) {
        
        double reembolsoEstimado = servicioPedidos.evaluarPeticionCancelacion(id, idCliente);
        return ResponseEntity.ok(new RespuestaGenericaDTO("Cancelación evaluada. Reembolso estimado: " + reembolsoEstimado + "€", id));
    }

    @PostMapping("/{id}/cancelar/confirmar")
    public ResponseEntity<RespuestaGenericaDTO> confirmarCancelacion(
            @PathVariable String id, 
            @RequestBody ConfirmarCancelacionRequest request) {
        
        servicioPedidos.ejecutarCancelacionDefinitiva(
                id, 
                request.getMontoReembolsoAcordado(), 
                request.getIdComercio(), 
                request.getIdCliente()
        );
        return ResponseEntity.ok(new RespuestaGenericaDTO("Cancelación ejecutada con éxito y fondos devueltos.", id));
    }
}
