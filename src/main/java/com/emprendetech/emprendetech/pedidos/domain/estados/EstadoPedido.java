package com.emprendetech.emprendetech.pedidos.domain.estados;

import com.emprendetech.emprendetech.pedidos.domain.Pedido;
import com.emprendetech.emprendetech.pedidos.domain.TransicionInvalidaException;

/**
 * Patrón State. Cada estado decide qué transiciones permite.
 * Por defecto todas están prohibidas; cada estado concreto habilita solo las suyas.
 */
public interface EstadoPedido {

    String nombre();

    default void pagar(Pedido pedido) {
        throw new TransicionInvalidaException("No se puede pagar un pedido en estado " + nombre());
    }

    default void enviar(Pedido pedido) {
        throw new TransicionInvalidaException("No se puede enviar un pedido en estado " + nombre());
    }

    default void entregar(Pedido pedido) {
        throw new TransicionInvalidaException("No se puede entregar un pedido en estado " + nombre());
    }

    static EstadoPedido desdeNombre(String nombre) {
        return switch (nombre) {
            case "PENDIENTE" -> new Pendiente();
            case "PAGADO" -> new Pagado();
            case "ENVIADO" -> new Enviado();
            case "ENTREGADO" -> new Entregado();
            default -> throw new IllegalArgumentException("Estado desconocido: " + nombre);
        };
    }
}
