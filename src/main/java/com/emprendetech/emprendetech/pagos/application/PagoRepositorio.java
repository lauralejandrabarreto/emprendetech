package com.emprendetech.emprendetech.pagos.application;

import com.emprendetech.emprendetech.pagos.domain.Pago;

import java.util.Optional;

public interface PagoRepositorio {

    Pago guardar(Pago pago);

    Optional<Pago> buscarPorReferencia(String referencia);
}
