package com.pedidosonline.proyecto_sw_2026.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "repartidores")
public class Repartidor {
    @Id
    private String idRepartidor;

    private String nombre;
    private String vehiculo;
    private String ubicacionTiempoReal; 
    private boolean estaDisponible;
}