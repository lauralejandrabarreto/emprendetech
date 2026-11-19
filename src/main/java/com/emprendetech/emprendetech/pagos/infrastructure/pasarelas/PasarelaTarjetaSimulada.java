package com.emprendetech.emprendetech.pagos.infrastructure.pasarelas;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Simula una pasarela de tarjetas. Recibe un token (la pasarela ya capturó la tarjeta
 * en el navegador) y el monto en centavos. El sistema nunca ve el número de la tarjeta (RNF6).
 */
@Component
public class PasarelaTarjetaSimulada {

    public record CargoRespuesta(String chargeId, String status) {
    }

    public CargoRespuesta crearCargo(String tokenTarjeta, long montoEnCentavos) {
        return new CargoRespuesta("TAR-" + UUID.randomUUID().toString().substring(0, 8), "pending");
    }
}
