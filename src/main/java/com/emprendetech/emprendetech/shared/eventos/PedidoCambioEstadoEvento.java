package com.emprendetech.emprendetech.shared.eventos;

/** Lo publica el módulo Pedidos cada vez que un pedido cambia de estado (lo usaría Reportes). */
public record PedidoCambioEstadoEvento(Long pedidoId, String estadoAnterior, String estadoNuevo) implements EventoDominio {
}
