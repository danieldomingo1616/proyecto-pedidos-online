package com.pedidosonline.proyecto_sw_2026.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "comercios")
public class Comercio {
    @Id
    private String idComercio;

    private String nombre;
    private String direccion;
    private String geolocalizacion; 
    private boolean estaAbierto;
}