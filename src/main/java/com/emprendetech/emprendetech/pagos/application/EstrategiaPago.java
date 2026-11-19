package com.emprendetech.emprendetech.pagos.application;

import com.emprendetech.emprendetech.pagos.domain.MetodoPago;
import com.emprendetech.emprendetech.pagos.domain.Pago;

/**
 * Patrón Strategy: interfaz común para todas las formas de pago.
 * También es el "puerto" de Clean Architecture: el caso de uso depende de esta interfaz,
 * y las implementaciones concretas (adaptadores) viven en infraestructura.
 * Agregar una pasarela nueva = crear una clase nueva que implemente esta interfaz (RNF5).
 */
public interface EstrategiaPago {

    MetodoPago metodo();

    /**
     * @param identificadorPagador celular (Nequi/Daviplata) o token de tarjeta generado por la pasarela.
     *                             Nunca el número de la tarjeta.
     */
    ResultadoCobro cobrar(Pago pago, String identificadorPagador);
}
