package com.sagag.eshop.repo.api.order;


import com.sagag.eshop.repo.entity.order.VFinalCustomerOrder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * View Final Customer Order JPA repository interface.
 */
public interface VFinalCustomerOrderRepository extends JpaRepository<VFinalCustomerOrder, Long>,
    JpaSpecificationExecutor<VFinalCustomerOrder> {

}
