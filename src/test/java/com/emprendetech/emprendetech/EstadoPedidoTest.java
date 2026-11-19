package com.emprendetech.emprendetech;

import com.emprendetech.emprendetech.pedidos.domain.Pedido;
import com.emprendetech.emprendetech.pedidos.domain.TransicionInvalidaException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/** Pruebas del patrón State sin Spring: el dominio funciona solo. */
class EstadoPedidoTest {

    @Test
    void recorreElFlujoNormal() {
        Pedido pedido = Pedido.nuevo(1L, "Chapinero", new BigDecimal("50000"), false);
        assertEquals("PENDIENTE", pedido.getEstado().nombre());
        pedido.pagar();
        assertEquals("PAGADO", pedido.getEstado().nombre());
        pedido.enviar();
        assertEquals("ENVIADO", pedido.getEstado().nombre());
        pedido.entregar();
        assertEquals("ENTREGADO", pedido.getEstado().nombre());
    }

    @Test
    void noPermiteEnviarUnPedidoSinPagar() {
        Pedido pedido = Pedido.nuevo(1L, "Suba", new BigDecimal("30000"), false);
        assertThrows(TransicionInvalidaException.class, pedido::enviar);
    }

    @Test
    void noPermitePagarDosVeces() {
        Pedido pedido = Pedido.nuevo(1L, "Kennedy", new BigDecimal("30000"), false);
        pedido.pagar();
        assertThrows(TransicionInvalidaException.class, pedido::pagar);
    }

    @Test
    void contraentregaSePuedeEnviarSinPagar() {
        Pedido pedido = Pedido.nuevo(1L, "Usaquén", new BigDecimal("30000"), true);
        pedido.enviar();
        assertEquals("ENVIADO", pedido.getEstado().nombre());
    }

    @Test
    void entregadoEsEstadoFinal() {
        Pedido pedido = Pedido.nuevo(1L, "Bosa", new BigDecimal("30000"), true);
        pedido.enviar();
        pedido.entregar();
        assertThrows(TransicionInvalidaException.class, pedido::entregar);
    }
}
