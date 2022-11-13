package com.sagag.eshop.repo.api.order;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.order.FinalCustomerOrder;
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

import java.util.List;

/**
 * Integration test class for final customer order repository.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
@Slf4j
public class FinalCustomerOrderRepositoryIT {

  @Autowired
  private FinalCustomerOrderRepository finalCustomerOrderRepo;

  @Test
  public void findAll_shouldReturnResult() {
    final long start = System.currentTimeMillis();
    final List<FinalCustomerOrder> orders = finalCustomerOrderRepo.findAll();

    log.debug("Perf: FinalCustomerOrderRepositoryIT -> findAll_shouldReturnResult {} ms",
        System.currentTimeMillis() - start);
    Assert.assertThat(orders, Matchers.not(Matchers.empty()));
  }

}
