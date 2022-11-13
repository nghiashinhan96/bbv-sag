package com.sagag.services.service.validator;

import com.sagag.eshop.repo.api.EshopUserRepository;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.service.SagServiceApplication;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test class for eshop user from Ax service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { SagServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class EshopUserFromAxServiceValidatorIT {

  @Autowired
  private EshopUserFromAxServiceValidator validator;

  @Autowired
  private EshopUserRepository eshopUserRepo;

  @Test
  public void shouldExistingCustomerFromAx() {
    final Long userId = 27L; // username = tuan1.ax
    final EshopUser eshopUser = eshopUserRepo.findById(userId).orElse(null);
    final String affiliate = SupportedAffiliate.DERENDINGER_AT.getAffiliate();
    boolean result = validator.validate(eshopUser, affiliate);
    Assert.assertThat(result, Matchers.is(true));
  }

}
