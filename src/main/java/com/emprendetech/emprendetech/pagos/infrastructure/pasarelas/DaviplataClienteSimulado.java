package com.emprendetech.emprendetech.pagos.infrastructure.pasarelas;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/** Simula la API de Daviplata. Responde con un mapa, distinto a Nequi. */
@Component
public class DaviplataClienteSimulado {

    public Map<String, String> crearTransaccion(String numeroCelular, BigDecimal monto) {
        return Map.of(
                "idTransaccion", "DAV-" + UUID.randomUUID().toString().substring(0, 8),
                "estado", "EN_PROCESO");
    }
}
