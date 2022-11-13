package com.sagag.eshop.repo.api.order;

import com.sagag.eshop.repo.entity.order.VFinalCustomerOrderStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * View Final customer order status repository interface.
 */
public interface VFinalCustomerOrderStatusRepository extends JpaRepository<VFinalCustomerOrderStatus, Long> {

  @Query("select f from VFinalCustomerOrderStatus f "
  + "where f.orgCode = :orgCode")
  List<VFinalCustomerOrderStatus> findOrderStatusByOrgCode(@Param("orgCode") Long orgCode);
}
