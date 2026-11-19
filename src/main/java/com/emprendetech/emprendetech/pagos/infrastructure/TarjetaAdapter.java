package com.emprendetech.emprendetech.pagos.infrastructure;

import com.emprendetech.emprendetech.pagos.application.EstrategiaPago;
import com.emprendetech.emprendetech.pagos.application.ResultadoCobro;
import com.emprendetech.emprendetech.pagos.domain.EstadoPago;
import com.emprendetech.emprendetech.pagos.domain.MetodoPago;
import com.emprendetech.emprendetech.pagos.domain.Pago;
import com.emprendetech.emprendetech.pagos.infrastructure.pasarelas.PasarelaTarjetaSimulada;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/** Patrón Adapter: convierte pesos a centavos y la respuesta del cargo a un ResultadoCobro. */
@Component
public class TarjetaAdapter implements EstrategiaPago {

    private final PasarelaTarjetaSimulada pasarela;

    public TarjetaAdapter(PasarelaTarjetaSimulada pasarela) {
        this.pasarela = pasarela;
    }

    @Override
    public MetodoPago metodo() {
        return MetodoPago.TARJETA;
    }

    @Override
    public ResultadoCobro cobrar(Pago pago, String tokenTarjeta) {
        long centavos = pago.getMonto().multiply(BigDecimal.valueOf(100)).longValueExact();
        PasarelaTarjetaSimulada.CargoRespuesta cargo = pasarela.crearCargo(tokenTarjeta, centavos);
        return new ResultadoCobro(EstadoPago.PENDIENTE_CONFIRMACION, cargo.chargeId());
    }
}
