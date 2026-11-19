package com.emprendetech.emprendetech.pedidos.domain.estados;

import com.emprendetech.emprendetech.pedidos.domain.Pedido;

public class Pagado implements EstadoPedido {

    @Override
    public String nombre() {
        return "PAGADO";
    }

    @Override
    public void enviar(Pedido pedido) {
        pedido.cambiarEstado(new Enviado());
    }
}
