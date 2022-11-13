package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.OrderStatus;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
}
