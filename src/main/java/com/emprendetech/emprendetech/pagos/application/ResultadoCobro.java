package com.emprendetech.emprendetech.pagos.application;

import com.emprendetech.emprendetech.pagos.domain.EstadoPago;

/** Respuesta común que toda estrategia de pago devuelve, venga de la pasarela que venga. */
public record ResultadoCobro(EstadoPago estado, String referencia) {
}
