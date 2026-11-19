package com.emprendetech.emprendetech.pedidos.domain.estados;

import com.emprendetech.emprendetech.pedidos.domain.Pedido;
import com.emprendetech.emprendetech.pedidos.domain.TransicionInvalidaException;

public class Pendiente implements EstadoPedido {

    @Override
    public String nombre() {
        return "PENDIENTE";
    }

    @Override
    public void pagar(Pedido pedido) {
        pedido.cambiarEstado(new Pagado());
    }

    /** Solo un pedido contraentrega puede salir sin estar pagado: se cobra al entregarlo. */
    @Override
    public void enviar(Pedido pedido) {
        if (!pedido.isContraentrega()) {
            throw new TransicionInvalidaException("El pedido debe estar pagado antes de enviarse");
        }
        pedido.cambiarEstado(new Enviado());
    }
}
