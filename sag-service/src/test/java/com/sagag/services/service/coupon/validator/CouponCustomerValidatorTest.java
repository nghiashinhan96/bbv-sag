package com.sagag.services.service.coupon.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sagag.eshop.repo.entity.CouponConditions;
import com.sagag.eshop.service.dto.OwnSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.service.coupon.CouponValidationData;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

@EshopMockitoJUnitRunner
public class CouponCustomerValidatorTest {

  @InjectMocks
  private CouponCustomerValidator validator;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void isInValid_shouldBeValid_givenValidData() throws Exception {
    CouponValidationData data = getValidData();
    assertFalse(validator.isInValid(data));
  }

  @Test
  public void isInValid_shouldBeInvalid_givenInvalidData() throws Exception {
    CouponValidationData data = getInvalidData();
    assertTrue(validator.isInValid(data));
  }

  public static CouponValidationData getValidData() {
    Customer customer = Customer.builder().nr(1100005).build();
    UserInfo user = new UserInfo();
    OwnSettings ownSettings = new OwnSettings();
    user.setCustomer(customer);
    user.setSettings(ownSettings);

    CouponConditions conditions = new CouponConditions();
    conditions.setCustomerNr("1100005");
    CouponValidationData data =
        CouponValidationData.builder().user(user).coupConditions(conditions).build();
    return data;
  }

  public static CouponValidationData getInvalidData() {
    Customer customer = Customer.builder().nr(1130438).build();
    UserInfo user = new UserInfo();
    OwnSettings ownSettings = new OwnSettings();
    user.setCustomer(customer);
    user.setSettings(ownSettings);

    CouponConditions conditions = new CouponConditions();
    conditions.setCustomerNr("1100005");
    CouponValidationData data =
        CouponValidationData.builder().user(user).coupConditions(conditions).build();
    return data;
  }
}
