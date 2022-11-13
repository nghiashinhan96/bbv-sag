package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.PriceDisplayType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repository for @{@link PriceDisplayType}.
 */
public interface PriceDisplayTypeRepository extends JpaRepository<PriceDisplayType, Integer> {
  
  @Query(value = "select p from PriceDisplayType p where p.type = :type")
  Optional<PriceDisplayType> findByType(@Param("type") final String type);
}
