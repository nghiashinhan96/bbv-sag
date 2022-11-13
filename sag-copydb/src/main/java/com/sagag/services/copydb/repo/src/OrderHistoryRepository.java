package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.OrderHistory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
}
