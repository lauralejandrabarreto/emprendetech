package com.emprendetech.emprendetech.pedidos.infrastructure;

import com.emprendetech.emprendetech.pedidos.application.PedidoRepositorio;
import com.emprendetech.emprendetech.pedidos.domain.Pedido;
import com.emprendetech.emprendetech.pedidos.domain.estados.EstadoPedido;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/** Adaptador de persistencia: implementa el puerto PedidoRepositorio usando JPA. */
@Repository
public class PedidoRepositorioJpa implements PedidoRepositorio {

    private final PedidoJpaRepository jpa;

    public PedidoRepositorioJpa(PedidoJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Pedido guardar(Pedido pedido) {
        PedidoEntity guardado = jpa.save(new PedidoEntity(pedido.getId(), pedido.getCompradorId(),
                pedido.getLocalidad(), pedido.getTotal(), pedido.isContraentrega(), pedido.getEstado().nombre()));
        pedido.asignarId(guardado.getId());
        return pedido;
    }

    @Override
    public Optional<Pedido> buscarPorId(Long id) {
        return jpa.findById(id).map(e -> new Pedido(e.getId(), e.getCompradorId(), e.getLocalidad(),
                e.getTotal(), e.isContraentrega(), EstadoPedido.desdeNombre(e.getEstado())));
    }
}
