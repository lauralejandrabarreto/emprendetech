package com.emprendetech.emprendetech.pagos.domain;

import com.emprendetech.emprendetech.shared.errores.ReglaNegocioException;

import java.math.BigDecimal;

/** Entidad de dominio del pago. No guarda datos de tarjeta, solo la referencia de la pasarela (RNF6). */
public class Pago {

    private Long id;
    private final Long pedidoId;
    private final BigDecimal monto;
    private final MetodoPago metodo;
    private EstadoPago estado;
    private String referencia;

    public Pago(Long id, Long pedidoId, BigDecimal monto, MetodoPago metodo, EstadoPago estado, String referencia) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.monto = monto;
        this.metodo = metodo;
        this.estado = estado;
        this.referencia = referencia;
    }

    public static Pago nuevo(Long pedidoId, BigDecimal monto, MetodoPago metodo) {
        if (monto == null || monto.signum() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero");
        }
        return new Pago(null, pedidoId, monto, metodo, null, null);
    }

    public void registrarRespuestaPasarela(EstadoPago estado, String referencia) {
        this.estado = estado;
        this.referencia = referencia;
    }

    public void aprobar() {
        exigirPendiente();
        this.estado = EstadoPago.APROBADO;
    }

    public void rechazar() {
        exigirPendiente();
        this.estado = EstadoPago.RECHAZADO;
    }

    private void exigirPendiente() {
        if (estado != EstadoPago.PENDIENTE_CONFIRMACION) {
            throw new ReglaNegocioException("El pago " + referencia + " ya fue resuelto (" + estado + ")");
        }
    }

    public Long getId() { return id; }
    public void asignarId(Long id) { this.id = id; }
    public Long getPedidoId() { return pedidoId; }
    public BigDecimal getMonto() { return monto; }
    public MetodoPago getMetodo() { return metodo; }
    public EstadoPago getEstado() { return estado; }
    public String getReferencia() { return referencia; }
}
