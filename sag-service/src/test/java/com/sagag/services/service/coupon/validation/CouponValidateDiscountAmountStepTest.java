package com.sagag.services.service.coupon.validation;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.entity.CouponConditions;
import com.sagag.eshop.service.dto.OwnSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponDiscountAmountValidator;
import com.sagag.services.service.exception.coupon.InvalidAffiliateCouponException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@EshopMockitoJUnitRunner
public class CouponValidateDiscountAmountStepTest {

  @InjectMocks
  private CouponValidateDiscountAmountStep couponValidateDiscountAmountStep;

  @Mock
  private CouponDiscountAmountValidator discountAmountValidator;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test(expected = InvalidAffiliateCouponException.class)
  public void handle_shouldThrowsException_givenInvalidData() throws Exception {
    Customer customer = Customer.builder().nr(1100005).build();
    UserInfo user = new UserInfo();
    OwnSettings ownSettings = new OwnSettings();
    user.setCustomer(customer);
    user.setSettings(ownSettings);
    user.setCompanyName("company_A");
    CouponConditions conditions = new CouponConditions();
    conditions.setDiscountArticleId(1001388532);
    CouponValidationData data =
        CouponValidationData.builder().user(user).coupConditions(conditions).build();
    when(discountAmountValidator.isInValid(data)).thenReturn(true);
    couponValidateDiscountAmountStep.handle(data);
  }

  @Test
  public void handle_shouldReturnOriginalData_givenValidData() throws Exception {
    Customer customer = Customer.builder().nr(1100005).build();
    UserInfo user = new UserInfo();
    OwnSettings ownSettings = new OwnSettings();
    user.setCustomer(customer);
    user.setSettings(ownSettings);
    user.setCompanyName("company_A");
    CouponConditions conditions = new CouponConditions();
    conditions.setDiscountArticleId(1001388532);
    CouponValidationData data =
        CouponValidationData.builder().user(user).coupConditions(conditions).build();
    when(discountAmountValidator.isInValid(data)).thenReturn(false);
    CouponValidationData results = couponValidateDiscountAmountStep.handle(data);
    assertNotNull(results);
  }
}
