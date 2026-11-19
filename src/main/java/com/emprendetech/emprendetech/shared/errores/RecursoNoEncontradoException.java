package com.emprendetech.emprendetech.shared.errores;

/** El recurso pedido no existe. Responde 404. */
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
