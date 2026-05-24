package com.pedidosonline.proyecto_sw_2026.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "reembolsos")
public class Reembolso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReembolso;

    private double montoReembolsado;
    private String motivo;
    private boolean esParcial;

    @OneToOne
    @JoinColumn(name = "pago_id")
    private Pago pago;
}