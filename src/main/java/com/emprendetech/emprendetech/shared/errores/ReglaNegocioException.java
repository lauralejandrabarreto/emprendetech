package com.emprendetech.emprendetech.shared.errores;

/** Se viola una regla del negocio (por ejemplo, una transición de estado no permitida). Responde 409. */
public class ReglaNegocioException extends RuntimeException {
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
