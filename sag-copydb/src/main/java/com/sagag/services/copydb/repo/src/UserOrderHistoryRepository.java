package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.UserOrderHistory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOrderHistoryRepository extends JpaRepository<UserOrderHistory, Long> {
}
