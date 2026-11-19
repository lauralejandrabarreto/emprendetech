package com.emprendetech.emprendetech.pagos.infrastructure;

import com.emprendetech.emprendetech.pagos.application.PagoRepositorio;
import com.emprendetech.emprendetech.pagos.domain.EstadoPago;
import com.emprendetech.emprendetech.pagos.domain.MetodoPago;
import com.emprendetech.emprendetech.pagos.domain.Pago;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PagoRepositorioJpa implements PagoRepositorio {

    private final PagoJpaRepository jpa;

    public PagoRepositorioJpa(PagoJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Pago guardar(Pago pago) {
        PagoEntity guardado = jpa.save(new PagoEntity(pago.getId(), pago.getPedidoId(), pago.getMonto(),
                pago.getMetodo().name(), pago.getEstado().name(), pago.getReferencia()));
        pago.asignarId(guardado.getId());
        return pago;
    }

    @Override
    public Optional<Pago> buscarPorReferencia(String referencia) {
        return jpa.findByReferencia(referencia).map(e -> new Pago(e.getId(), e.getPedidoId(), e.getMonto(),
                MetodoPago.valueOf(e.getMetodo()), EstadoPago.valueOf(e.getEstado()), e.getReferencia()));
    }
}
