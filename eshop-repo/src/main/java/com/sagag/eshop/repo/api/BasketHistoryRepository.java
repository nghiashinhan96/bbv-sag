package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.BasketHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

public interface BasketHistoryRepository extends JpaRepository<BasketHistory, Long>,
  JpaSpecificationExecutor<BasketHistory> {

  List<BasketHistory> findByUpdatedDateBefore(Date updatedDate);

}
