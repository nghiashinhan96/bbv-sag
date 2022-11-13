package com.sagag.services.service.coupon.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.entity.CouponConditions;
import com.sagag.eshop.service.dto.OwnSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.ivds.api.IvdsArticleService;
import com.sagag.services.service.coupon.CouponValidationData;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

@EshopMockitoJUnitRunner
public class CouponDiscountAmountValidatorTest {

  @InjectMocks
  private CouponDiscountAmountValidator validator;

  @Mock
  private IvdsArticleService ivdsArticleService;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void isInValid_shouldBeValid_givenValidData() throws Exception {
    Customer customer = Customer.builder().nr(1100005).build();
    OwnSettings ownSetting = new OwnSettings();
    UserInfo user = new UserInfo();
    user.setCustomer(customer);
    user.setSettings(ownSetting);
    user.setCompanyName("company_A");
    user.setSettings(ownSetting);
    CouponConditions conditions = new CouponConditions();
    conditions.setDiscountArticleId(1001388532);
    CouponValidationData data =
        CouponValidationData.builder().user(user).coupConditions(conditions).build();
    when(ivdsArticleService.getCounponPrice(user, String.valueOf(conditions.getDiscountArticleId())))
        .thenReturn(Optional.of(100.00));

    assertFalse(validator.isInValid(data));
  }

  @Test
  public void isInValid_shouldBeInvalid_givenInvalidData() throws Exception {
    Customer customer = Customer.builder().nr(1100005).build();
    UserInfo user = new UserInfo();
    OwnSettings ownSetting = new OwnSettings();
    user.setCustomer(customer);
    user.setSettings(ownSetting);
    user.setCompanyName("company_A");
    user.setSettings(ownSetting);
    CouponConditions conditions = new CouponConditions();
    conditions.setDiscountArticleId(1001388532);
    CouponValidationData data =
        CouponValidationData.builder().user(user).coupConditions(conditions).build();
    when(ivdsArticleService.getCounponPrice(user, String.valueOf(conditions.getDiscountArticleId())))
    .thenReturn(Optional.of(00.00));

    assertTrue(validator.isInValid(data));
  }
}
