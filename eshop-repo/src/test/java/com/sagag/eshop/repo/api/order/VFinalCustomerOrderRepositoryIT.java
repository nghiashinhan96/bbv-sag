package com.sagag.eshop.repo.api.order;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.criteria.finalcustomer.FinalCustomerOrderCriteria;
import com.sagag.eshop.repo.entity.order.VFinalCustomerOrder;
import com.sagag.eshop.repo.specification.VFinalCustomerOrderSpecification;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.FinalCustomerOrderStatus;
import com.sagag.services.common.utils.PageUtils;
import java.util.Arrays;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test class for View final customer order repository.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class VFinalCustomerOrderRepositoryIT {

  @Autowired
  private VFinalCustomerOrderRepository repository;

  @Test
  public void findAll_shouldReturnResult() {
    final FinalCustomerOrderCriteria criteria = FinalCustomerOrderCriteria.builder()
        .statuses(Arrays.asList(FinalCustomerOrderStatus.OPEN.name()))
        .finalOrgIds(Arrays.asList(139L))
        .build();

    final Page<VFinalCustomerOrder> page = repository.findAll(
      VFinalCustomerOrderSpecification.of(criteria), PageUtils.DEF_PAGE);

    Assert.assertThat(page.getTotalElements(),
      Matchers.greaterThanOrEqualTo(0L));
    Assert.assertThat(page.getContent().get(0).getTotalGrossPrice(),
      Matchers.greaterThanOrEqualTo(0D));
  }

}
