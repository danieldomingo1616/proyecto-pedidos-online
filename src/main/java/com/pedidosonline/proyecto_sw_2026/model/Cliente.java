package com.pedidosonline.proyecto_sw_2026.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String idCliente;

    private String nombre;
    private String email;
    private String telefono;
    private String ubicacionActual;
}