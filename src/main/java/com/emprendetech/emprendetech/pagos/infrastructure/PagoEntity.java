package com.emprendetech.emprendetech.pagos.infrastructure;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "pagos")
public class PagoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long pedidoId;
    private BigDecimal monto;
    private String metodo;
    private String estado;
    @Column(unique = true)
    private String referencia;

    protected PagoEntity() {
    }

    public PagoEntity(Long id, Long pedidoId, BigDecimal monto, String metodo, String estado, String referencia) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.monto = monto;
        this.metodo = metodo;
        this.estado = estado;
        this.referencia = referencia;
    }

    public Long getId() { return id; }
    public Long getPedidoId() { return pedidoId; }
    public BigDecimal getMonto() { return monto; }
    public String getMetodo() { return metodo; }
    public String getEstado() { return estado; }
    public String getReferencia() { return referencia; }
}
