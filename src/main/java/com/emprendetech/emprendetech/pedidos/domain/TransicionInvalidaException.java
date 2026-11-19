package com.emprendetech.emprendetech.pedidos.domain;

import com.emprendetech.emprendetech.shared.errores.ReglaNegocioException;

public class TransicionInvalidaException extends ReglaNegocioException {
    public TransicionInvalidaException(String mensaje) {
        super(mensaje);
    }
}
