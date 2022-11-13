package com.sagag.services.tools.repository.target;


import com.sagag.services.tools.domain.target.CollectiveDelivery;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollectiveDeliveryRepository extends JpaRepository<CollectiveDelivery, Integer> {
  Optional<CollectiveDelivery> findOneById(int id);
}
