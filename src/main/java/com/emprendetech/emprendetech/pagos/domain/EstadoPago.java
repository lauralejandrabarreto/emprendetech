package com.emprendetech.emprendetech.pagos.domain;

public enum EstadoPago {
    /** La pasarela recibió la solicitud y falta su confirmación por webhook. */
    PENDIENTE_CONFIRMACION,
    APROBADO,
    RECHAZADO,
    /** Efectivo: se cobra al momento de la entrega. */
    CONTRAENTREGA
}
