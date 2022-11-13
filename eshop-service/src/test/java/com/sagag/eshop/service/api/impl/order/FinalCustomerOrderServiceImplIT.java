package com.sagag.eshop.service.api.impl.order;

import com.sagag.eshop.repo.api.order.FinalCustomerOrderRepository;
import com.sagag.eshop.repo.entity.order.FinalCustomerOrder;
import com.sagag.eshop.service.api.FinalCustomerOrderService;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * IT for Final Customer Order.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class FinalCustomerOrderServiceImplIT {

  @Autowired
  private FinalCustomerOrderService service;

  @Autowired
  private FinalCustomerOrderRepository finalCustomerOrderRepository;

  private static final String DELETED = "DELETED";
  private static final String OPEN = "OPEN";

  @Test
  public void deleteOrder_shouldChangeStatus_givenOpenOrderId() {
    final long openOrderId = 1;
    service.deleteOrder(openOrderId);
    Optional<FinalCustomerOrder> finalCustomerOrder =
        finalCustomerOrderRepository.findById(openOrderId);
    if (finalCustomerOrder.isPresent()) {
      Assert.assertTrue(finalCustomerOrder.get().getStatus().equalsIgnoreCase(DELETED));
    }
  }

  @Test
  public void updateOrderStatus_shouldChangeStatus_givenOpenOrderId() {
    final long orderId = 2;
    service.changeOrderStatusToOpen(orderId);
    Optional<FinalCustomerOrder> finalCustomerOrder =
        finalCustomerOrderRepository.findById(orderId);
    if (finalCustomerOrder.isPresent()) {
      Assert.assertTrue(finalCustomerOrder.get().getStatus().equalsIgnoreCase(OPEN));
    }
  }


}
