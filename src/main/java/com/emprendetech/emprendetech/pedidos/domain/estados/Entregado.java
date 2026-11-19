package com.emprendetech.emprendetech.pedidos.domain.estados;

/** Estado final: no permite ninguna transición. */
public class Entregado implements EstadoPedido {

    @Override
    public String nombre() {
        return "ENTREGADO";
    }
}
