package com.emprendetech.emprendetech;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/** Prueba el flujo completo por la API REST: Strategy + Adapter + Observer + State juntos. */
@SpringBootTest
class FlujoCompraIntegracionTest {

    @Autowired
    WebApplicationContext contexto;

    MockMvc mvc;

    @BeforeEach
    void preparar() {
        mvc = MockMvcBuilders.webAppContextSetup(contexto).build();
    }

    /** Lee un campo de la respuesta JSON sin depender de una librería JSON concreta. */
    private static String campo(String json, String nombre) {
        Matcher m = Pattern.compile("\"" + nombre + "\"\\s*:\\s*\"?([^\",}]*)\"?").matcher(json);
        assertTrue(m.find(), "No se encontró el campo " + nombre + " en " + json);
        return m.group(1);
    }

    private String enviarJson(String metodo, String url, String cuerpo, int estadoEsperado) throws Exception {
        var peticion = switch (metodo) {
            case "POST" -> post(url);
            case "PATCH" -> patch(url);
            default -> get(url);
        };
        if (cuerpo != null) {
            peticion.contentType(MediaType.APPLICATION_JSON).content(cuerpo);
        }
        var respuesta = mvc.perform(peticion).andReturn().getResponse();
        assertEquals(estadoEsperado, respuesta.getStatus(), respuesta.getContentAsString());
        return respuesta.getContentAsString();
    }

    private String crearPedido(boolean contraentrega) throws Exception {
        String json = enviarJson("POST", "/api/pedidos",
                "{\"compradorId\":1,\"localidad\":\"Chapinero\",\"total\":85000,\"contraentrega\":" + contraentrega + "}", 201);
        assertEquals("PENDIENTE", campo(json, "estado"));
        return campo(json, "id");
    }

    private String pagar(String pedidoId, String metodo, String identificador) throws Exception {
        return enviarJson("POST", "/api/pagos", "{\"pedidoId\":" + pedidoId + ",\"monto\":85000,\"metodo\":\""
                + metodo + "\",\"identificadorPagador\":\"" + identificador + "\"}", 201);
    }

    @Test
    void pagoConfirmadoPorWebhookMarcaElPedidoComoPagado() throws Exception {
        String pedidoId = crearPedido(false);

        String referencia = campo(pagar(pedidoId, "NEQUI", "3001234567"), "referencia");
        assertTrue(referencia.startsWith("NEQ-"));

        String pago = enviarJson("POST", "/api/pagos/webhook",
                "{\"referencia\":\"" + referencia + "\",\"aprobado\":true}", 200);
        assertEquals("APROBADO", campo(pago, "estado"));

        // Observer: el módulo Pedidos reaccionó al evento de Pagos
        assertEquals("PAGADO", campo(enviarJson("GET", "/api/pedidos/" + pedidoId, null, 200), "estado"));
        assertEquals("ENVIADO", campo(enviarJson("PATCH", "/api/pedidos/" + pedidoId + "/enviar", null, 200), "estado"));
        assertEquals("ENTREGADO", campo(enviarJson("PATCH", "/api/pedidos/" + pedidoId + "/entregar", null, 200), "estado"));
    }

    @Test
    void cadaMetodoUsaSuPropiaEstrategia() throws Exception {
        assertTrue(campo(pagar(crearPedido(false), "DAVIPLATA", "3109876543"), "referencia").startsWith("DAV-"));
        assertTrue(campo(pagar(crearPedido(false), "TARJETA", "tok_visa_123"), "referencia").startsWith("TAR-"));
        assertEquals("CONTRAENTREGA", campo(pagar(crearPedido(true), "EFECTIVO", ""), "estado"));
    }

    @Test
    void transicionInvalidaResponde409() throws Exception {
        String pedidoId = crearPedido(false);
        enviarJson("PATCH", "/api/pedidos/" + pedidoId + "/entregar", null, 409);
    }
}