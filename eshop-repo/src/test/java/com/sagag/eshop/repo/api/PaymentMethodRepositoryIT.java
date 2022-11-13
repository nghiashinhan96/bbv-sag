package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.PaymentMethod;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Integration test class for {@link PaymentMethodRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@Slf4j
@EshopIntegrationTest
@Transactional
public class PaymentMethodRepositoryIT {

  @Autowired
  private PaymentMethodRepository paymentMethodRepo;

  @Test
  public void testfindOneByid() {
    log.debug("starting PaymentMethodsRepository");
    final Optional<PaymentMethod> paymentMethod = paymentMethodRepo.findOneByDescCode("CASH");
    Assert.assertThat(2, Is.is(paymentMethod.get().getId()));
  }

  @Test
  public void shouldDisplayAllPaymentMethodWithOrderDisplay() {
    final List<PaymentMethod> methods = paymentMethodRepo.findAllOrderByOrderDisplayAsc();
    Assert.assertThat(methods.isEmpty(), Matchers.is(false));
    Assert.assertThat(methods.get(0).getDescCode(), Matchers.equalTo("CREDIT"));
    Assert.assertThat(methods.get(1).getDescCode(), Matchers.equalTo("CASH"));
    Assert.assertThat(methods.get(2).getDescCode(), Matchers.equalTo("DIRECT_INVOICE"));
    Assert.assertThat(methods.get(3).getDescCode(), Matchers.equalTo("CARD"));
  }
}
