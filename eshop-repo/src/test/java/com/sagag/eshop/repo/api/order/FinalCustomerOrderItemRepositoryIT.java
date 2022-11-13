package com.sagag.eshop.repo.api.order;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.order.FinalCustomerOrderItem;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Integration test class for final customer order item repository.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
@Slf4j
public class FinalCustomerOrderItemRepositoryIT {

  @Autowired
  private FinalCustomerOrderItemRepository finalCustomerOrderItemRepo;

  @Test
  public void findAll_shouldReturnResult() {
    final long start = System.currentTimeMillis();
    final List<FinalCustomerOrderItem> orderItems = finalCustomerOrderItemRepo.findAll();

    log.debug("Perf: FinalCustomerOrderItemRepositoryIT -> findAll_shouldReturnResult {} ms",
        System.currentTimeMillis() - start);
    Assert.assertThat(orderItems, Matchers.not(Matchers.empty()));
  }

  @Test
  public void findByFinalCustomerOrderId_shouldReturnResult_givenOrderStatusNew() {
    final long start = System.currentTimeMillis();
    final Long userId = 1L;
    final List<FinalCustomerOrderItem> orderItems =
        finalCustomerOrderItemRepo.findByFinalCustomerOrderIds(Arrays.asList(userId));

    log.debug(
        "Perf: FinalCustomerOrderItemRepositoryIT -> findByFinalCustomerOrderId_shouldReturnResult_givenOrderStatusNew {} ms",
        System.currentTimeMillis() - start);
    Assert.assertThat(orderItems, Matchers.not(Matchers.empty()));
  }

  @Test
  public void findByFinalCustomerOrderId_shouldReturnResult_givenFinalCustomerOrderId() {
    final long start = System.currentTimeMillis();
    final List<FinalCustomerOrderItem> orderItems =
        finalCustomerOrderItemRepo.findByFinalCustomerOrderIds(Arrays.asList(1L));

    log.debug(
        "Perf: FinalCustomerOrderItemRepositoryIT -> findByFinalCustomerOrderId_shouldReturnResult_givenFinalCustomerOrderId {} ms",
        System.currentTimeMillis() - start);
    Assert.assertThat(orderItems, Matchers.not(Matchers.empty()));
  }

}
