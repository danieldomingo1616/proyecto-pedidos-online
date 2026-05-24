package com.pedidosonline.proyecto_sw_2026.service;

import com.pedidosonline.proyecto_sw_2026.infrastructure.MessageBrokerFake;
import com.pedidosonline.proyecto_sw_2026.infrastructure.WebSocketServiceFake;
import com.pedidosonline.proyecto_sw_2026.model.Pedido;
import com.pedidosonline.proyecto_sw_2026.repository.BBDDPedidos;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ServicioPedidos {

    private final BBDDPedidos bbddPedidos;
    private final ServicioPagos pagos;
    private final MessageBrokerFake broker;
    private final WebSocketServiceFake webSockets;
    private final GestorReglasNegocio reglas;

    public ServicioPedidos(BBDDPedidos bbddPedidos, ServicioPagos pagos, 
                           MessageBrokerFake broker, WebSocketServiceFake webSockets, 
                           GestorReglasNegocio reglas) {
        this.bbddPedidos = bbddPedidos;
        this.pagos = pagos;
        this.broker = broker;
        this.webSockets = webSockets;
        this.reglas = reglas;
    }

    // CU 1: Crear la venta inicial
    public String procesarNuevaCompra(double total, String metodoPago, String idComercio, String idCliente) {
        // 1. Finanzas: Pre-autorización
        String token = pagos.solicitarPreAutorizacion(total, metodoPago);

        // 2. Persistencia: Crear y guardar el pedido (Patrón Creador)
        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setIdPedido("PED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        nuevoPedido.setEstado("PENDIENTE_CONFIRMACION");
        nuevoPedido.setMontoTotal(total);
        nuevoPedido.setFechaHora(LocalDateTime.now());
        
        bbddPedidos.save(nuevoPedido); // ¡Llamada a la base de datos!

        // 3. Infraestructura Asíncrona: Eventos y Alertas
        broker.publicarEvento("NuevoPedido", nuevoPedido.getIdPedido());
        webSockets.pushNotification(idComercio, "Nuevo pedido entrante: " + nuevoPedido.getIdPedido());

        return nuevoPedido.getIdPedido();
    }

    // CU 1: El comercio acepta prepararlo
    public void confirmarPreparacion(String idPedido, String tokenPreauth, String idCliente) {
        Pedido pedido = buscarPedidoOpcional(idPedido);
        
        pagos.capturarFondos(tokenPreauth);
        
        pedido.setEstado("EN_PREPARACION");
        bbddPedidos.save(pedido); // Actualizamos la BBDD

        broker.publicarEvento("PedidoEnPreparacion", idPedido);
        webSockets.pushNotification(idCliente, "Tu pedido " + idPedido + " se está cocinando. ETA: 25 min.");
    }

    // CU 3: Falta de stock
    public void registrarIncidenciaStock(String idPedido, String idProducto, String idCliente) {
        Pedido pedido = buscarPedidoOpcional(idPedido);
        broker.publicarEvento("PausarAsignacionReparto", idPedido);
        webSockets.solicitarResolucionCliente(idCliente, idProducto, 5.50);
    }

    // CU 3: Cliente acepta eliminar el producto faltante
    public void aplicarResolucionEliminar(String idPedido, double diferenciaEconomica, String idComercio) {
        pagos.procesarReembolso("TX-" + idPedido, diferenciaEconomica);
        webSockets.pushNotification(idComercio, "Reanudar preparación. Cliente aceptó eliminar producto.");
        broker.publicarEvento("ReactivarAsignacionReparto", idPedido);
    }

    // CU 4: Mostrar al cliente lo que le costará cancelar
    public double evaluarPeticionCancelacion(String idPedido, String idCliente) {
        Pedido pedido = buscarPedidoOpcional(idPedido);
        
        double porcentajeReembolso = reglas.evaluarPoliticaCancelacion(pedido.getEstado());
        double reembolsoEstimado = pedido.getMontoTotal() * porcentajeReembolso;
        
        return Math.round(reembolsoEstimado * 100.0) / 100.0;
    }

    // CU 4: Ejecutar la cancelación definitiva
    public void ejecutarCancelacionDefinitiva(String idPedido, double montoReembolso, String idComercio, String idCliente) {
        Pedido pedido = buscarPedidoOpcional(idPedido);
        
        if (montoReembolso > 0) {
            pagos.procesarReembolso("TX-" + idPedido, montoReembolso);
        }

        pedido.setEstado("CANCELADO");
        bbddPedidos.save(pedido);

        webSockets.pushNotification(idComercio, "Abortar preparación del pedido " + idPedido + " - CANCELADO por usuario.");
    }

    // Método privado de utilidad para evitar código repetido (Alta Cohesión)
    private Pedido buscarPedidoOpcional(String idPedido) {
        return bbddPedidos.findById(idPedido)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe un pedido con ID: " + idPedido
                ));
    }
}
