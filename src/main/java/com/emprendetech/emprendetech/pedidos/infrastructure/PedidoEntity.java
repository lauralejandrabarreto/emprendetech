package com.emprendetech.emprendetech.pedidos.infrastructure;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

/** Representación del pedido en la base de datos. Solo existe en la capa de infraestructura. */
@Entity
@Table(name = "pedidos")
public class PedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long compradorId;
    private String localidad;
    private BigDecimal total;
    private boolean contraentrega;
    private String estado;

    protected PedidoEntity() {
    }

    public PedidoEntity(Long id, Long compradorId, String localidad, BigDecimal total,
                        boolean contraentrega, String estado) {
        this.id = id;
        this.compradorId = compradorId;
        this.localidad = localidad;
        this.total = total;
        this.contraentrega = contraentrega;
        this.estado = estado;
    }

    public Long getId() { return id; }
    public Long getCompradorId() { return compradorId; }
    public String getLocalidad() { return localidad; }
    public BigDecimal getTotal() { return total; }
    public boolean isContraentrega() { return contraentrega; }
    public String getEstado() { return estado; }
}
