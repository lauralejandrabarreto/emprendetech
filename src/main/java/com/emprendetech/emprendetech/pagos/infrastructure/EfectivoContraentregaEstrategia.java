package com.emprendetech.emprendetech.pagos.infrastructure;

import com.emprendetech.emprendetech.pagos.application.EstrategiaPago;
import com.emprendetech.emprendetech.pagos.application.ResultadoCobro;
import com.emprendetech.emprendetech.pagos.domain.EstadoPago;
import com.emprendetech.emprendetech.pagos.domain.MetodoPago;
import com.emprendetech.emprendetech.pagos.domain.Pago;
import org.springframework.stereotype.Component;

import java.util.UUID;

/** Estrategia sin pasarela externa (por eso no es un Adapter): el cobro se hace al entregar. */
@Component
public class EfectivoContraentregaEstrategia implements EstrategiaPago {

    @Override
    public MetodoPago metodo() {
        return MetodoPago.EFECTIVO;
    }

    @Override
    public ResultadoCobro cobrar(Pago pago, String identificadorPagador) {
        return new ResultadoCobro(EstadoPago.CONTRAENTREGA, "EFE-" + UUID.randomUUID().toString().substring(0, 8));
    }
}
