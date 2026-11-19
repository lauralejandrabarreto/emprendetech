package com.emprendetech.emprendetech.pedidos.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

interface PedidoJpaRepository extends JpaRepository<PedidoEntity, Long> {
}
