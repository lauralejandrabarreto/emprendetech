package com.emprendetech.emprendetech.pedidos.domain;

import com.emprendetech.emprendetech.pedidos.domain.estados.EstadoPedido;
import com.emprendetech.emprendetech.pedidos.domain.estados.Pendiente;

import java.math.BigDecimal;

/**
 * Entidad de dominio. No tiene anotaciones de Spring ni de JPA: la capa de dominio
 * no depende de ningún framework (regla de dependencia de Clean Architecture).
 * El pedido delega las transiciones a su estado actual (patrón State).
 */
public class Pedido {

    private Long id;
    private final Long compradorId;
    private final String localidad;
    private final BigDecimal total;
    private final boolean contraentrega;
    private EstadoPedido estado;

    public Pedido(Long id, Long compradorId, String localidad, BigDecimal total,
                  boolean contraentrega, EstadoPedido estado) {
        this.id = id;
        this.compradorId = compradorId;
        this.localidad = localidad;
        this.total = total;
        this.contraentrega = contraentrega;
        this.estado = estado;
    }

    public static Pedido nuevo(Long compradorId, String localidad, BigDecimal total, boolean contraentrega) {
        if (total == null || total.signum() <= 0) {
            throw new IllegalArgumentException("El total del pedido debe ser mayor que cero");
        }
        return new Pedido(null, compradorId, localidad, total, contraentrega, new Pendiente());
    }

    public void pagar() {
        estado.pagar(this);
    }

    public void enviar() {
        estado.enviar(this);
    }

    public void entregar() {
        estado.entregar(this);
    }

    /** Solo lo llaman los estados. */
    public void cambiarEstado(EstadoPedido nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public Long getId() { return id; }
    public void asignarId(Long id) { this.id = id; }
    public Long getCompradorId() { return compradorId; }
    public String getLocalidad() { return localidad; }
    public BigDecimal getTotal() { return total; }
    public boolean isContraentrega() { return contraentrega; }
    public EstadoPedido getEstado() { return estado; }
}
