package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.CollectiveDelivery;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollectiveDeliveryRepository extends JpaRepository<CollectiveDelivery, Integer> {
  Optional<CollectiveDelivery> findOneById(int id);
}
