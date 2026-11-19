package com.emprendetech.emprendetech.pedidos.application;

import com.emprendetech.emprendetech.pedidos.domain.Pedido;
import com.emprendetech.emprendetech.shared.eventos.PedidoCambioEstadoEvento;
import com.emprendetech.emprendetech.shared.eventos.PublicadorEventos;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.function.Consumer;

/** Casos de uso del módulo Pedidos (RF6). */
@Service
public class ServicioPedidos {

    private final PedidoRepositorio repositorio;
    private final PublicadorEventos publicador;

    public ServicioPedidos(PedidoRepositorio repositorio, PublicadorEventos publicador) {
        this.repositorio = repositorio;
        this.publicador = publicador;
    }

    @Transactional
    public Pedido crear(Long compradorId, String localidad, BigDecimal total, boolean contraentrega) {
        return repositorio.guardar(Pedido.nuevo(compradorId, localidad, total, contraentrega));
    }

    @Transactional(readOnly = true)
    public Pedido consultar(Long id) {
        return repositorio.buscarPorId(id).orElseThrow(() -> new PedidoNoEncontradoException(id));
    }

    @Transactional
    public Pedido marcarPagado(Long id) {
        return aplicarTransicion(id, Pedido::pagar);
    }

    @Transactional
    public Pedido enviar(Long id) {
        return aplicarTransicion(id, Pedido::enviar);
    }

    @Transactional
    public Pedido entregar(Long id) {
        return aplicarTransicion(id, Pedido::entregar);
    }

    private Pedido aplicarTransicion(Long id, Consumer<Pedido> transicion) {
        Pedido pedido = consultar(id);
        String anterior = pedido.getEstado().nombre();
        transicion.accept(pedido);
        Pedido guardado = repositorio.guardar(pedido);
        publicador.publicar(new PedidoCambioEstadoEvento(id, anterior, guardado.getEstado().nombre()));
        return guardado;
    }
}
