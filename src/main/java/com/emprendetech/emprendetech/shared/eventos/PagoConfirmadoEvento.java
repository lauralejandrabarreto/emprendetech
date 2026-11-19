package com.emprendetech.emprendetech.shared.eventos;

/** Lo publica el módulo Pagos cuando una pasarela confirma el cobro. */
public record PagoConfirmadoEvento(Long pedidoId, String referencia) implements EventoDominio {
}
