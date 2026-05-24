package com.pedidosonline.proyecto_sw_2026.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "pedidos")
public class Pedido {
    @Id
    private String idPedido;

    private LocalDateTime fechaHora;
    
    private String estado; 
    
    private double montoTotal;
    private int tiempoEstimadoEntrega; 

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "comercio_id")
    private Comercio comercio;

    @ManyToOne
    @JoinColumn(name = "repartidor_id")
    private Repartidor repartidor; 

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pago_id")
    private Pago pago;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "pedido_id")
    private List<LineaPedido> lineas; 

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "pedido_id")
    private List<IncidenciaStock> incidencias; 
}