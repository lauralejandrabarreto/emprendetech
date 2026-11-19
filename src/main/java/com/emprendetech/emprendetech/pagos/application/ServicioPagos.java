package com.emprendetech.emprendetech.pagos.application;

import com.emprendetech.emprendetech.pagos.domain.MetodoPago;
import com.emprendetech.emprendetech.pagos.domain.Pago;
import com.emprendetech.emprendetech.shared.eventos.PagoConfirmadoEvento;
import com.emprendetech.emprendetech.shared.errores.RecursoNoEncontradoException;
import com.emprendetech.emprendetech.shared.eventos.PublicadorEventos;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Casos de uso del módulo Pagos (RF5). Es el "contexto" del patrón Strategy:
 * elige la estrategia según el método de pago sin usar if/else por pasarela.
 */
@Service
public class ServicioPagos {

    private final Map<MetodoPago, EstrategiaPago> estrategias = new EnumMap<>(MetodoPago.class);
    private final PagoRepositorio repositorio;
    private final PublicadorEventos publicador;

    /** Spring entrega todas las clases que implementan EstrategiaPago. */
    public ServicioPagos(List<EstrategiaPago> disponibles, PagoRepositorio repositorio, PublicadorEventos publicador) {
        disponibles.forEach(e -> estrategias.put(e.metodo(), e));
        this.repositorio = repositorio;
        this.publicador = publicador;
    }

    @Transactional
    public Pago procesar(Long pedidoId, BigDecimal monto, MetodoPago metodo, String identificadorPagador) {
        EstrategiaPago estrategia = estrategias.get(metodo);
        if (estrategia == null) {
            throw new IllegalArgumentException("Método de pago no soportado: " + metodo);
        }
        Pago pago = Pago.nuevo(pedidoId, monto, metodo);
        ResultadoCobro resultado = estrategia.cobrar(pago, identificadorPagador);
        pago.registrarRespuestaPasarela(resultado.estado(), resultado.referencia());
        return repositorio.guardar(pago);
    }

    /** La pasarela avisa por webhook si el cobro se aprobó o se rechazó. */
    @Transactional
    public Pago confirmarDesdeWebhook(String referencia, boolean aprobado) {
        Pago pago = repositorio.buscarPorReferencia(referencia)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un pago con referencia " + referencia));
        if (aprobado) {
            pago.aprobar();
        } else {
            pago.rechazar();
        }
        Pago guardado = repositorio.guardar(pago);
        if (aprobado) {
            // Observer: Pagos solo avisa; no sabe quién está escuchando.
            publicador.publicar(new PagoConfirmadoEvento(guardado.getPedidoId(), referencia));
        }
        return guardado;
    }
}
