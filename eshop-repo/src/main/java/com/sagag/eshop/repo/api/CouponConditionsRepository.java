package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.CouponConditions;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponConditionsRepository extends JpaRepository<CouponConditions, Integer> {
  Optional<CouponConditions> findOneByCouponsCode(String couponsCode);
}
