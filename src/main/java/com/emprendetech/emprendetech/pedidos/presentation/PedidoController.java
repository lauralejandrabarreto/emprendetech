package com.emprendetech.emprendetech.pedidos.presentation;

import com.emprendetech.emprendetech.pedidos.application.ServicioPedidos;
import com.emprendetech.emprendetech.pedidos.domain.Pedido;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final ServicioPedidos servicio;

    public PedidoController(ServicioPedidos servicio) {
        this.servicio = servicio;
    }

    public record CrearPedidoRequest(@NotNull Long compradorId, @NotBlank String localidad,
                                     @NotNull @Positive BigDecimal total, boolean contraentrega) {
    }

    public record PedidoResponse(Long id, Long compradorId, String localidad, BigDecimal total,
                                 boolean contraentrega, String estado) {
        static PedidoResponse de(Pedido p) {
            return new PedidoResponse(p.getId(), p.getCompradorId(), p.getLocalidad(), p.getTotal(),
                    p.isContraentrega(), p.getEstado().nombre());
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponse crear(@Valid @RequestBody CrearPedidoRequest req) {
        return PedidoResponse.de(servicio.crear(req.compradorId(), req.localidad(), req.total(), req.contraentrega()));
    }

    @GetMapping("/{id}")
    public PedidoResponse consultar(@PathVariable Long id) {
        return PedidoResponse.de(servicio.consultar(id));
    }

    @PatchMapping("/{id}/enviar")
    public PedidoResponse enviar(@PathVariable Long id) {
        return PedidoResponse.de(servicio.enviar(id));
    }

    @PatchMapping("/{id}/entregar")
    public PedidoResponse entregar(@PathVariable Long id) {
        return PedidoResponse.de(servicio.entregar(id));
    }
}
