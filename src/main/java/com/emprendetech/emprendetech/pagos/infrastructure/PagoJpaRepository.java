package com.emprendetech.emprendetech.pagos.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface PagoJpaRepository extends JpaRepository<PagoEntity, Long> {
    Optional<PagoEntity> findByReferencia(String referencia);
}
