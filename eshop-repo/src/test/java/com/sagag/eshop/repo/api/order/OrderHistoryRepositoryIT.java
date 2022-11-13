package com.sagag.eshop.repo.api.order;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.order.OrderHistory;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Integration test class for Order history repository.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class OrderHistoryRepositoryIT {

  @Autowired
  private OrderHistoryRepository orderHistoryRepo;

  @Test
  public void givenClosedDate_shouldUpdateSucceed() {
    final long orderId = 1;
    final Date closedDate = Calendar.getInstance().getTime();
    orderHistoryRepo.updateClosedDateByOrderId(orderId, closedDate);
  }

  @Test
  public void testFindLatestOrderStateByUserId() {
    orderHistoryRepo.findLatestOrderStateByUserId(27L);
  }

  @Test
  public void testFindAvailableOrderNrs() {
    final List<String> orderNrs = Arrays.asList("AU3010013520", "AU3010000045");
    final List<String> availOrderNrs = orderHistoryRepo.findAvailableOrderNrs(orderNrs, 26L);
    Assert.assertThat(availOrderNrs.isEmpty(), Matchers.is(false));
    Assert.assertThat(availOrderNrs, Matchers.containsInAnyOrder("AU3010013520"));
  }

  @Test
  public void testFindByOrderNumber() {
    final String orderNr = "AU3010013520";
    Optional<OrderHistory> orderHistory = orderHistoryRepo.findByOrderNumber(orderNr);
    Assert.assertThat(orderHistory.isPresent(), Matchers.is(true));
    Assert.assertThat(orderHistory.get().getOrderNumber(), Matchers.is(orderNr));
    assertOne(orderHistory.get());
  }

  private static void assertOne(OrderHistory orderHistory) {
    Assert.assertThat(orderHistory.getId(), Matchers.notNullValue());
    Assert.assertThat(orderHistory.getOrderNumber(),
      Matchers.anyOf(Matchers.nullValue(), Matchers.notNullValue()));
    Assert.assertThat(orderHistory.getTransNumber(),
      Matchers.anyOf(Matchers.nullValue(), Matchers.notNullValue()));
    Assert.assertThat(orderHistory.getCreatedDate(), Matchers.notNullValue());
    Assert.assertThat(orderHistory.getSaleName(),
      Matchers.anyOf(Matchers.nullValue(), Matchers.notNullValue()));
    Assert.assertThat(orderHistory.getTotal(), Matchers.notNullValue());
    Assert.assertThat(orderHistory.getOrderInfoJson(), Matchers.notNullValue());
    Assert.assertThat(orderHistory.getErpOrderDetailUrl(), Matchers.notNullValue());
    Assert.assertThat(orderHistory.getUserId(), Matchers.notNullValue());
    Assert.assertThat(orderHistory.getSaleId(),
      Matchers.anyOf(Matchers.nullValue(), Matchers.notNullValue()));
    Assert.assertThat(orderHistory.getClosedDate(),
      Matchers.anyOf(Matchers.nullValue(), Matchers.notNullValue()));
    Assert.assertThat(orderHistory.getType(), Matchers.notNullValue());
    Assert.assertThat(orderHistory.getAxProcessStatus(),
      Matchers.anyOf(Matchers.nullValue(), Matchers.notNullValue()));
    Assert.assertThat(orderHistory.getWorkIds(),
      Matchers.anyOf(Matchers.nullValue(), Matchers.notNullValue()));
    Assert.assertThat(orderHistory.getOrderState(),
      Matchers.anyOf(Matchers.nullValue(), Matchers.notNullValue()));
  }

}
