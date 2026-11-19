package com.emprendetech.emprendetech.pagos.infrastructure.pasarelas;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Simula el SDK de Nequi. En producción haría la llamada HTTP real.
 * Su API es propia: recibe el celular y el valor en pesos como número entero.
 */
@Component
public class NequiClienteSimulado {

    public String solicitarCobroPush(String celular, long valorEnPesos) {
        return "NEQ-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
