package com.emprendetech.emprendetech.pagos.infrastructure;

import com.emprendetech.emprendetech.pagos.application.EstrategiaPago;
import com.emprendetech.emprendetech.pagos.application.ResultadoCobro;
import com.emprendetech.emprendetech.pagos.domain.EstadoPago;
import com.emprendetech.emprendetech.pagos.domain.MetodoPago;
import com.emprendetech.emprendetech.pagos.domain.Pago;
import com.emprendetech.emprendetech.pagos.infrastructure.pasarelas.DaviplataClienteSimulado;
import org.springframework.stereotype.Component;

import java.util.Map;

/** Patrón Adapter: traduce la respuesta en mapa de Daviplata a un ResultadoCobro. */
@Component
public class DaviplataAdapter implements EstrategiaPago {

    private final DaviplataClienteSimulado daviplata;

    public DaviplataAdapter(DaviplataClienteSimulado daviplata) {
        this.daviplata = daviplata;
    }

    @Override
    public MetodoPago metodo() {
        return MetodoPago.DAVIPLATA;
    }

    @Override
    public ResultadoCobro cobrar(Pago pago, String celular) {
        Map<String, String> respuesta = daviplata.crearTransaccion(celular, pago.getMonto());
        return new ResultadoCobro(EstadoPago.PENDIENTE_CONFIRMACION, respuesta.get("idTransaccion"));
    }
}
