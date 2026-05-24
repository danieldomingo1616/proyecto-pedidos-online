package com.pedidosonline.proyecto_sw_2026.mapper;

import com.pedidosonline.proyecto_sw_2026.dto.LineaPedidoDTO;
import com.pedidosonline.proyecto_sw_2026.dto.PedidoDTO;
import com.pedidosonline.proyecto_sw_2026.model.LineaPedido;
import com.pedidosonline.proyecto_sw_2026.model.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// componentModel = "spring" permite inyectarlo luego con el constructor
@Mapper(componentModel = "spring")
public interface PedidoMapper {

    // Mapeamos atributos que se llaman distinto entre la Entidad y el DTO
    @Mapping(source = "comercio.nombre", target = "nombreComercio")
    @Mapping(source = "repartidor.nombre", target = "nombreRepartidor")
    PedidoDTO toDto(Pedido pedido);

    @Mapping(source = "producto.nombre", target = "nombreProducto")
    LineaPedidoDTO toLineaDto(LineaPedido linea);
}