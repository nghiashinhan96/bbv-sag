package com.sagag.services.copydb.repo.dest;

import com.sagag.services.copydb.domain.dest.DestOrderHistory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DestOrderHistoryRepository extends JpaRepository<DestOrderHistory, Long> {
}
