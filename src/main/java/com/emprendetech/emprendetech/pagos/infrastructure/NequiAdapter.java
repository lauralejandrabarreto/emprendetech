package com.emprendetech.emprendetech.pagos.infrastructure;

import com.emprendetech.emprendetech.pagos.application.EstrategiaPago;
import com.emprendetech.emprendetech.pagos.application.ResultadoCobro;
import com.emprendetech.emprendetech.pagos.domain.EstadoPago;
import com.emprendetech.emprendetech.pagos.domain.MetodoPago;
import com.emprendetech.emprendetech.pagos.domain.Pago;
import com.emprendetech.emprendetech.pagos.infrastructure.pasarelas.NequiClienteSimulado;
import org.springframework.stereotype.Component;

/** Patrón Adapter: traduce la API de Nequi a la interfaz EstrategiaPago del sistema. */
@Component
public class NequiAdapter implements EstrategiaPago {

    private final NequiClienteSimulado nequi;

    public NequiAdapter(NequiClienteSimulado nequi) {
        this.nequi = nequi;
    }

    @Override
    public MetodoPago metodo() {
        return MetodoPago.NEQUI;
    }

    @Override
    public ResultadoCobro cobrar(Pago pago, String celular) {
        String id = nequi.solicitarCobroPush(celular, pago.getMonto().longValueExact());
        return new ResultadoCobro(EstadoPago.PENDIENTE_CONFIRMACION, id);
    }
}
