package com.sagag.eshop.repo.api.order;


import com.sagag.eshop.repo.entity.order.FinalCustomerOrderItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Final Customer Order Item JPA repository interface.
 */
public interface FinalCustomerOrderItemRepository
    extends JpaRepository<FinalCustomerOrderItem, Long> {

  @Query("select f from FinalCustomerOrderItem f where f.finalCustomerOrderId = :id")
  List<FinalCustomerOrderItem> findByFinalCustomerOrderId(@Param("id") Long id);

  @Query("select f from FinalCustomerOrderItem f where f.finalCustomerOrderId IN :ids")
  List<FinalCustomerOrderItem> findByFinalCustomerOrderIds(@Param("ids") List<Long> ids);

}
