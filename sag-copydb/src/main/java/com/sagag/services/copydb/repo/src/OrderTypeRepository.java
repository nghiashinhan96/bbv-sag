package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.OrderType;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTypeRepository extends JpaRepository<OrderType, Integer> {
}
