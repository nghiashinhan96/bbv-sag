package com.sagag.services.service.api.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.service.SagServiceApplication;
import com.sagag.services.service.api.CouponBusinessService;
import com.sagag.services.service.exception.coupon.CouponValidationException;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * IT for {@link CouponBusinessService}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { SagServiceApplication.class })
@EshopIntegrationTest
@Transactional
@Ignore("Please put some business test cases in here")
public class CouponBusinessServiceImplIT {

  @Autowired
  private CouponBusinessService couponBusService;

  @Test
  public void shouldThrowNotFoundCouponCode() throws CouponValidationException {
    final String couponCode = "ABC";
    couponBusService.checkCouponsAvailable(couponCode, new UserInfo(), null);
  }
}
