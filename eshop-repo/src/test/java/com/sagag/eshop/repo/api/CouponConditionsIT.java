package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.CouponConditions;
import com.sagag.services.common.annotation.EshopIntegrationTest;

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
 * Integration test class for {@link CouponConditionsRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class CouponConditionsIT {

  @Autowired
  private CouponConditionsRepository couponConditionsRepo;

  @Test
  public void testfindAll() {
    final List<CouponConditions> couponConditions = couponConditionsRepo.findAll();
    Assert.assertThat(couponConditions.isEmpty(), Is.is(false));
    Assert.assertThat("TECHNOMAG", Is.is(couponConditions.get(0).getAffiliate()));
  }

  @Test
  public void testfindOneByCouponsCode() {
    String code = "DEV0001";
    final Optional<CouponConditions> conditions = couponConditionsRepo.findOneByCouponsCode(code);
    Assert.assertThat(true, Is.is(conditions.isPresent()));
    Assert.assertThat("TECHNOMAG", Is.is(conditions.get().getAffiliate()));
  }
}
