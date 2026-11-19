package com.emprendetech.emprendetech.shared.eventos;

/** Patrón Observer: rol de observador. Cada módulo interesado implementa esta interfaz. */
public interface SuscriptorEvento<E extends EventoDominio> {

    Class<E> tipoDeEvento();

    void alRecibir(E evento);
}
