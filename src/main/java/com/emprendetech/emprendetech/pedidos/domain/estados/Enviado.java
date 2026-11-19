package com.emprendetech.emprendetech.pedidos.domain.estados;

import com.emprendetech.emprendetech.pedidos.domain.Pedido;

public class Enviado implements EstadoPedido {

    @Override
    public String nombre() {
        return "ENVIADO";
    }

    @Override
    public void entregar(Pedido pedido) {
        pedido.cambiarEstado(new Entregado());
    }
}
