package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.CouponUseLog;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.hamcrest.Matchers;
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
 * Integration test class for {@link CouponUseLogRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class CouponUseLogRepositoryIT {

  @Autowired
  private CouponUseLogRepository couponUseLogRepo;

  @Test
  public void testFindAll() {
    final List<CouponUseLog> couponUseLog = couponUseLogRepo.findAll();
    Assert.assertNotNull(couponUseLog);
  }


  @Test
  public void testFindByUserId() {

    final Optional<CouponUseLog> couponUseLog = couponUseLogRepo.findOneByUserId("1");
    Assert.assertNotNull(couponUseLog);
  }

  @Test
  public void shouldExistsCouponCode() {
    final String custNr = "1100005";
    final String couponCode = "HUYNDAI";
    boolean result = couponUseLogRepo.existsCouponCodeByCustomerNr(custNr, couponCode);
    Assert.assertThat(result, Matchers.is(true));
  }

  @Test
  public void shouldNotExistsCouponCode() {
    final String custNr = "1100005";
    final String couponCode = "HUYNDAI_T"; // Not found coupon code
    boolean result = couponUseLogRepo.existsCouponCodeByCustomerNr(custNr, couponCode);
    Assert.assertThat(result, Matchers.is(false));
  }


}
