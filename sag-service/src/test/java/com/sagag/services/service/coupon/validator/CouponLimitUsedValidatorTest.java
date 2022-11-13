package com.sagag.services.service.coupon.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.CouponUseLogRepository;
import com.sagag.eshop.repo.entity.CouponConditions;
import com.sagag.eshop.service.dto.OwnSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.service.coupon.CouponValidationData;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@EshopMockitoJUnitRunner
public class CouponLimitUsedValidatorTest {

  private static final String COUPON_CODE = "CP01";

  @InjectMocks
  private CouponLimitUsedValidator validator;

  @Mock
  private CouponUseLogRepository couponUseLogRepo;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void isInValid_shouldBeValid_givenValidData() throws Exception {
    CouponValidationData data = getData();
    when(couponUseLogRepo.existsCouponCodeByCustomerNr("1100005", COUPON_CODE)).thenReturn(false);
    assertFalse(validator.isInValid(data));
  }

  @Test
  public void isInValid_shouldBeInvalid_givenInvalidData() throws Exception {
    CouponValidationData data = getData();
    when(couponUseLogRepo.existsCouponCodeByCustomerNr("1100005", COUPON_CODE)).thenReturn(true);
    assertTrue(validator.isInValid(data));
  }

  public static CouponValidationData getData() {
    Customer customer = Customer.builder().nr(1100005).build();
    UserInfo user = new UserInfo();
    OwnSettings ownSettings = new OwnSettings();
    user.setCustomer(customer);
    user.setSettings(ownSettings);

    CouponConditions conditions = new CouponConditions();
    conditions.setSingleCustomer(true);
    CouponValidationData data = CouponValidationData.builder().couponCode(COUPON_CODE).user(user)
        .coupConditions(conditions).build();
    return data;
  }
}
