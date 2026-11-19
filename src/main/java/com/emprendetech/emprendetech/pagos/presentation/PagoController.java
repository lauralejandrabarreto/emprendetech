package com.emprendetech.emprendetech.pagos.presentation;

import com.emprendetech.emprendetech.pagos.application.ServicioPagos;
import com.emprendetech.emprendetech.pagos.domain.MetodoPago;
import com.emprendetech.emprendetech.pagos.domain.Pago;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final ServicioPagos servicio;

    public PagoController(ServicioPagos servicio) {
        this.servicio = servicio;
    }

    public record ProcesarPagoRequest(@NotNull Long pedidoId, @NotNull @Positive BigDecimal monto,
                                      @NotNull MetodoPago metodo, String identificadorPagador) {
    }

    public record WebhookRequest(@NotBlank String referencia, boolean aprobado) {
    }

    public record PagoResponse(Long id, Long pedidoId, BigDecimal monto, MetodoPago metodo,
                               String estado, String referencia) {
        static PagoResponse de(Pago p) {
            return new PagoResponse(p.getId(), p.getPedidoId(), p.getMonto(), p.getMetodo(),
                    p.getEstado().name(), p.getReferencia());
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PagoResponse procesar(@Valid @RequestBody ProcesarPagoRequest req) {
        return PagoResponse.de(servicio.procesar(req.pedidoId(), req.monto(), req.metodo(), req.identificadorPagador()));
    }

    /** Endpoint que llamaría la pasarela para confirmar el cobro. En el PMV se invoca a mano. */
    @PostMapping("/webhook")
    public PagoResponse webhook(@Valid @RequestBody WebhookRequest req) {
        return PagoResponse.de(servicio.confirmarDesdeWebhook(req.referencia(), req.aprobado()));
    }
}
