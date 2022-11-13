package com.sagag.eshop.repo.api.order;

import com.sagag.eshop.repo.entity.order.VOrderHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VOrderHistoryRepository extends JpaRepository<VOrderHistory, Long>,
    JpaSpecificationExecutor<VOrderHistory> {
}
