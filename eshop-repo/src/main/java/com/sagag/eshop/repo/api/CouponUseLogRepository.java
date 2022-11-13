package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.CouponUseLog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CouponUseLogRepository extends JpaRepository<CouponUseLog, Integer> {

  Optional<CouponUseLog> findOneByUserId(String userId);

  /**
   * Checks exists coupon code by customer number.
   *
   *  @param customerNr the customer number
   *  @param couponCode the coupon code
   *  @return the true if exists, otherwise is false
   */
  @Query(value = "select case when count(l) > 0 then 'true' else 'false' end "
      + "from CouponUseLog l where l.customerNr = :customerNr and l.couponsCode = :couponCode")
  boolean existsCouponCodeByCustomerNr(@Param("customerNr") String customerNr,
      @Param("couponCode") String couponCode);

}
