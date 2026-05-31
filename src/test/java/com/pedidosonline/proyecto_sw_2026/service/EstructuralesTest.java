package com.pedidosonline.proyecto_sw_2026.service;

import com.pedidosonline.proyecto_sw_2026.infrastructure.MessageBrokerFake;
import com.pedidosonline.proyecto_sw_2026.infrastructure.WebSocketServiceFake;
import com.pedidosonline.proyecto_sw_2026.model.Pedido;
import com.pedidosonline.proyecto_sw_2026.repository.BBDDPedidos;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EstructuralesTest {

    // =========================================================
    // Pruebas Estructurales: GestorReglasNegocio
    // =========================================================
    
    private final GestorReglasNegocio gestorReglas = new GestorReglasNegocio();

    @Test
    public void testEvaluarPolitica_Pendiente() {
        assertEquals(1.0, gestorReglas.evaluarPoliticaCancelacion("PENDIENTE_CONFIRMACION"));
    }

    @Test
    public void testEvaluarPolitica_EnPreparacion() {
        assertEquals(0.5, gestorReglas.evaluarPoliticaCancelacion("EN_PREPARACION"));
    }

    @Test
    public void testEvaluarPolitica_EnCamino() {
        assertEquals(0.0, gestorReglas.evaluarPoliticaCancelacion("EN_CAMINO"));
    }

    @Test
    public void testEvaluarPolitica_EstadoDesconocido() {
        // Según tu código, el default devuelve 0.0
        assertEquals(0.0, gestorReglas.evaluarPoliticaCancelacion("ESTADO_INVENTADO"));
    }

    @Test
    public void testEvaluarPolitica_EstadoNull() {
        // Según tu código, si es null devuelve 0.0
        assertEquals(0.0, gestorReglas.evaluarPoliticaCancelacion(null));
    }

    // =========================================================
    // Pruebas Estructurales: ServicioPedidos.ejecutarCancelacionDefinitiva
    // =========================================================
    
    @Mock
    private BBDDPedidos bbddPedidos;
    @Mock
    private ServicioPagos pagos;
    @Mock
    private MessageBrokerFake broker;
    @Mock
    private WebSocketServiceFake webSockets;
    @Mock
    private GestorReglasNegocio reglas;

    @InjectMocks
    private ServicioPedidos servicioPedidos;

    @Test
    public void testEjecutarCancelacion_PedidoNoExiste() {
        // Camino 1: buscarPedidoOpcional lanza excepción
        String idPedido = "PED-FALSO";
        when(bbddPedidos.findById(idPedido)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            servicioPedidos.ejecutarCancelacionDefinitiva(idPedido, 15.0, "COM-1", "CLI-1");
        });
    }

    @Test
    public void testEjecutarCancelacion_ConReembolsoMayorACero() {
        // Camino 2: Entra en el if (montoReembolso > 0)
        String idPedido = "PED-1234";
        Pedido pedido = new Pedido();
        pedido.setIdPedido(idPedido);
        pedido.setEstado("EN_PREPARACION");
        
        when(bbddPedidos.findById(idPedido)).thenReturn(Optional.of(pedido));

        // Ejecutamos pasándole 15.50 de reembolso
        servicioPedidos.ejecutarCancelacionDefinitiva(idPedido, 15.50, "COM-1", "CLI-1");

        // Verificamos que procesarReembolso se llamó exactamente 1 vez
        verify(pagos, times(1)).procesarReembolso("TX-" + idPedido, 15.50);
        // Verificamos que se actualizó y guardó el estado
        assertEquals("CANCELADO", pedido.getEstado());
        verify(bbddPedidos, times(1)).save(pedido);
        // Verificamos notificación
        verify(webSockets, times(1)).pushNotification(eq("COM-1"), anyString());
    }

    @Test
    public void testEjecutarCancelacion_SinReembolso() {
        // Camino 3: NO entra en el if (montoReembolso > 0) porque es 0.0
        String idPedido = "PED-5678";
        Pedido pedido = new Pedido();
        pedido.setIdPedido(idPedido);
        
        when(bbddPedidos.findById(idPedido)).thenReturn(Optional.of(pedido));

        // Ejecutamos pasándole 0.0 de reembolso
        servicioPedidos.ejecutarCancelacionDefinitiva(idPedido, 0.0, "COM-2", "CLI-2");

        // Verificamos que NUNCA se procesó un reembolso
        verify(pagos, never()).procesarReembolso(anyString(), anyDouble());
        // Pero sí se canceló y notificó
        assertEquals("CANCELADO", pedido.getEstado());
        verify(bbddPedidos, times(1)).save(pedido);
        verify(webSockets, times(1)).pushNotification(eq("COM-2"), anyString());
    }
}