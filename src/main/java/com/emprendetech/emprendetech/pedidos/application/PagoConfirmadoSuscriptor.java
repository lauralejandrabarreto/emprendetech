package com.emprendetech.emprendetech.pedidos.application;

import com.emprendetech.emprendetech.shared.eventos.PagoConfirmadoEvento;
import com.emprendetech.emprendetech.shared.eventos.PublicadorEventos;
import com.emprendetech.emprendetech.shared.eventos.SuscriptorEvento;
import org.springframework.stereotype.Component;

/**
 * Patrón Observer: Pedidos reacciona cuando Pagos confirma un cobro.
 * Pagos nunca llama a Pedidos directamente; solo publica el evento.
 */
@Component
public class PagoConfirmadoSuscriptor implements SuscriptorEvento<PagoConfirmadoEvento> {

    private final ServicioPedidos servicioPedidos;

    public PagoConfirmadoSuscriptor(ServicioPedidos servicioPedidos, PublicadorEventos publicador) {
        this.servicioPedidos = servicioPedidos;
        publicador.suscribir(this);
    }

    @Override
    public Class<PagoConfirmadoEvento> tipoDeEvento() {
        return PagoConfirmadoEvento.class;
    }

    @Override
    public void alRecibir(PagoConfirmadoEvento evento) {
        servicioPedidos.marcarPagado(evento.pedidoId());
    }
}
