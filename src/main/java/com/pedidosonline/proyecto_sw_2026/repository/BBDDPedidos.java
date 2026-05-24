package com.pedidosonline.proyecto_sw_2026.repository;

import com.pedidosonline.proyecto_sw_2026.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BBDDPedidos extends JpaRepository<Pedido, String> {
    
    // Al heredar de JpaRepository<Pedido, String>, Spring automáticamente 
    // te regala todos los métodos que necesitas para tus Casos de Uso:
    //
    // 1. Para el CU 1 (guardarPedido): 
    //    Usaremos el método heredado .save(pedido)
    //
    // 2. Para el CU 1 y CU 4 (actualizarEstado / setEstado): 
    //    También se usa .save(pedido) cuando el objeto ya existe con un ID.
    //
    // 3. Para el CU 4 (obtenerEstadoActual): 
    //    Usaremos el método heredado .findById(idPedido)
}
