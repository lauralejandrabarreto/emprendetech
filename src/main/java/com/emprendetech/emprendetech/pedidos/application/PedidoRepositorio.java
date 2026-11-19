package com.emprendetech.emprendetech.pedidos.application;

import com.emprendetech.emprendetech.pedidos.domain.Pedido;

import java.util.Optional;

/** Puerto de salida: el caso de uso define qué necesita; la infraestructura decide cómo (JPA). */
public interface PedidoRepositorio {

    Pedido guardar(Pedido pedido);

    Optional<Pedido> buscarPorId(Long id);
}
