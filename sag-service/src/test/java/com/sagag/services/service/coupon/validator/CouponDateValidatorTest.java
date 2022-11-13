package com.sagag.services.service.coupon.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sagag.eshop.repo.entity.CouponConditions;
import com.sagag.services.service.coupon.CouponValidationData;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Calendar;
import java.util.Date;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Calendar.class)
public class CouponDateValidatorTest {

  @InjectMocks
  private CouponDateValidator couponDateValidator;

  @Test
  public void isInValid_shouldBeInvalid_givenInvalidData() throws Exception {
    CouponValidationData data = getInvalidData();
    assertTrue(couponDateValidator.isInValid(data));
  }

  @Test
  public void isInValid_shouldBeValid_givenValidData() throws Exception {
    CouponValidationData data = getValidData();
    assertFalse(couponDateValidator.isInValid(data));
  }

  public static CouponValidationData getInvalidData() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, 2017);
    calendar.set(Calendar.MONTH, 1);
    calendar.set(Calendar.DATE, 1);
    Date from = calendar.getTime();
    calendar.set(Calendar.MONTH, 2);
    calendar.set(Calendar.DATE, 2);
    Date to = calendar.getTime();

    CouponConditions conditions = new CouponConditions();
    conditions.setDateStart(from);
    conditions.setDateEnd(to);
    CouponValidationData data = CouponValidationData.builder().coupConditions(conditions).build();
    return data;
  }


  public static CouponValidationData getValidData() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, 2017);
    calendar.set(Calendar.MONTH, 1);
    calendar.set(Calendar.DATE, 1);
    Date from = calendar.getTime();
    calendar.set(Calendar.YEAR, 2117);
    Date to = calendar.getTime();

    CouponConditions conditions = new CouponConditions();
    conditions.setDateStart(from);
    conditions.setDateEnd(to);
    CouponValidationData data = CouponValidationData.builder().coupConditions(conditions).build();
    return data;
  }
}
