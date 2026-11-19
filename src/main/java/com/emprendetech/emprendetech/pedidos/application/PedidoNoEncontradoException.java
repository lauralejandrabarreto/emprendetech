package com.emprendetech.emprendetech.pedidos.application;

import com.emprendetech.emprendetech.shared.errores.RecursoNoEncontradoException;

public class PedidoNoEncontradoException extends RecursoNoEncontradoException {
    public PedidoNoEncontradoException(Long id) {
        super("No existe el pedido " + id);
    }
}
