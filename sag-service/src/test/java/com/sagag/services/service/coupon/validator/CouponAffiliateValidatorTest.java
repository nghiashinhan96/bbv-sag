package com.sagag.services.service.coupon.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sagag.eshop.repo.entity.CouponConditions;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.service.coupon.CouponValidationData;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

@EshopMockitoJUnitRunner
public class CouponAffiliateValidatorTest {

  @InjectMocks
  private CouponAffiliateValidator validator;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void isInValid_shoulBeValid_givenValidData() throws Exception {
    assertFalse(validator.isInValid(getValidData()));
  }

  @Test
  public void isInValid_shoulBeInvalid_givenInvalidData() throws Exception {
    assertTrue(validator.isInValid(getInvalidData()));
  }

  public static CouponValidationData getValidData() {
    UserInfo user = new UserInfo();
    user.setAffiliateShortName("derendinger-at");
    CouponConditions conditions = new CouponConditions();
    conditions.setAffiliate("derendinger-at");
    return CouponValidationData.builder().user(user).coupConditions(conditions).build();
  }

  public static CouponValidationData getInvalidData() {
    UserInfo user = new UserInfo();
    user.setAffiliateShortName("derendinger-at");
    CouponConditions conditions = new CouponConditions();
    conditions.setAffiliate("matik-at");
    return CouponValidationData.builder().user(user).coupConditions(conditions).build();
  }
}
