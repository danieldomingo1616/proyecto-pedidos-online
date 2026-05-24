package com.pedidosonline.proyecto_sw_2026.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pagos")
public class Pago {
    @Id
    private String idTransaccion;

    private String metodoPago;
    private double monto;
    private String estadoAutorizacion;

    @OneToOne(mappedBy = "pago", cascade = CascadeType.ALL)
    private Reembolso reembolso; 
}