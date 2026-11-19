package com.emprendetech.emprendetech.shared.eventos;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Patrón Observer: rol de sujeto. Los módulos publican eventos aquí sin saber quién los escucha,
 * y los interesados se registran con suscribir().
 */
@Component
public class PublicadorEventos {

    private final List<SuscriptorEvento<?>> suscriptores = new CopyOnWriteArrayList<>();

    public void suscribir(SuscriptorEvento<?> suscriptor) {
        suscriptores.add(suscriptor);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void publicar(EventoDominio evento) {
        for (SuscriptorEvento suscriptor : suscriptores) {
            if (suscriptor.tipoDeEvento().isInstance(evento)) {
                suscriptor.alRecibir(evento);
            }
        }
    }
}
