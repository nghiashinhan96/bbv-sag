package com.sagag.eshop.repo.api.order;


import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.order.VCustomerOrderHistory;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import lombok.extern.slf4j.Slf4j;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Integration test class for VCustomerOrderHistory repository.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
@Slf4j
public class VCustomerOrderHistoryRepositoryIT {

  @Autowired
  private VCustomerOrderHistoryRepository vCustomerOrderHistoryRepo;

  @Test
  public void testFindOrderByCustomerAndDate() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, 2017);
    calendar.set(Calendar.MONTH, 5);
    calendar.set(Calendar.DATE, 28);
    Date dateFrom = calendar.getTime();
    calendar.set(Calendar.MONTH, 8);
    calendar.set(Calendar.DATE, 1);
    Date dateTo = calendar.getTime();
    final long start = System.currentTimeMillis();
    final List<VCustomerOrderHistory> orders =
        vCustomerOrderHistoryRepo.findOrderByCustomerAndDate(1130438L, dateFrom, dateTo);
    log.debug("Perf: VCustomerOrderHistoryRepositoryIT -> testFindOrderByCustomerAndDate {} ms",
        System.currentTimeMillis() - start);
    Assert.assertNotNull(orders);
  }

  @Test
  public void testFindOrderByUserAndDate() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, 2017);
    calendar.set(Calendar.MONTH, 5);
    calendar.set(Calendar.DATE, 28);
    Date dateFrom = calendar.getTime();
    calendar.set(Calendar.MONTH, 8);
    calendar.set(Calendar.DATE, 1);
    Date dateTo = calendar.getTime();
    final long start = System.currentTimeMillis();
    final List<VCustomerOrderHistory> orders =
        vCustomerOrderHistoryRepo.findOrderByUserAndDate(27L, dateFrom, dateTo);
    log.debug("Perf: VCustomerOrderHistoryRepositoryIT -> testFindOrderByUserAndDate {} ms",
        System.currentTimeMillis() - start);
    Assert.assertNotNull(orders);
  }
}
