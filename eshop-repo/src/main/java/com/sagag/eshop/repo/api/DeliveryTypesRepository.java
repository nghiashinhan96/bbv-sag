package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.DeliveryType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Interface register signatures to works with DeliveryType.
 *
 */
public interface DeliveryTypesRepository extends JpaRepository<DeliveryType, Integer> {

  Optional<DeliveryType> findOneById(int id);

  Optional<DeliveryType> findOneByDescCode(String descCode);

  List<DeliveryType> findByDescCodeIn(List<String> descCodes);
}
