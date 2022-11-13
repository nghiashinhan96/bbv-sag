package com.sagag.eshop.repo.api.order;


import com.sagag.eshop.repo.entity.order.FinalCustomerOrder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Final Customer Order JPA repository interface.
 */
public interface FinalCustomerOrderRepository extends JpaRepository<FinalCustomerOrder, Long> {

  @Query("select o.orgId from FinalCustomerOrder o where o.id = :orderId")
  Optional<Long> findOrgIdByOrderId(@Param("orderId") Long orderId);
}
