package com.sagag.services.service.payment;

import com.google.common.collect.Lists;
import com.sagag.eshop.repo.api.CustomerSettingsRepository;
import com.sagag.eshop.repo.api.VUserDetailRepository;
import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.PaymentMethod;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.eshop.repo.entity.VUserDetail;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.api.UserSettingsService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.eshop.dto.PaymentMethodDto;
import com.sagag.services.domain.eshop.dto.PaymentSettingDto;
import com.sagag.services.hazelcast.api.UserCacheService;
import com.sagag.services.service.payment.impl.TmUserPaymentSettingsFormBuilderImpl;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class TmUserPaymentSettingsFormBuilderTest {

  private static final String CREDIT = "CREDIT";

  @InjectMocks
  private TmUserPaymentSettingsFormBuilderImpl userPaymentSettingsFormBuilder;

  @Mock
  private UserCacheService userCacheService;

  @Mock
  private CustomerSettingsRepository customerSettingsRepo;

  @Mock
  private UserSettingsService userSettingsService;

  @Mock
  private VUserDetailRepository vUserDetailRepo;

  @Mock
  private UserService userService;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuildUserPaymentSetting_shouldFail() {
    final long userId = 27L;
    final boolean salesOnBehalf = false;
    when(vUserDetailRepo.findByUserId(userId)).thenReturn(Optional.empty());
    userPaymentSettingsFormBuilder.buildUserPaymentSetting(userId, salesOnBehalf);
  }

  @Test
  public void testBuildUserPaymentSetting_shouldReturnUserPaymenSetting() {
    final long userId = 27L;
    final int userSettingId = 33;
    final boolean salesOnBehalf = false;
    final UserInfo finalCustomerUser = initFinalCustomerUserInfo();
    when(userCacheService.get(userId)).thenReturn(finalCustomerUser);

    final VUserDetail vUserDetail = initVUserDetail(userSettingId);
    final PaymentSettingDto paymentSetting = initPaymentSettingDto();
    final CustomerSettings customerSettings = initCustomerSetting();
    when(vUserDetailRepo.findByUserId(userId)).thenReturn(Optional.of(vUserDetail));
    when(userService.getPaymentSetting(salesOnBehalf, false)).thenReturn(paymentSetting);
    when(customerSettingsRepo.findById(Mockito.any())).thenReturn(Optional.of(customerSettings));
    when(userSettingsService.findUserSettingsById(userSettingId))
        .thenReturn(Optional.of(new UserSettings()));
    final PaymentSettingDto userPaymentSetting =
        userPaymentSettingsFormBuilder.buildUserPaymentSetting(userId, salesOnBehalf);

    Assert.assertNotNull(userPaymentSetting);
  }

  private CustomerSettings initCustomerSetting() {
    final CustomerSettings customerSettings = new CustomerSettings();
    final PaymentMethod paymentMethod = new PaymentMethod();
    paymentMethod.setDescCode(CREDIT);
    customerSettings.setPaymentMethod(paymentMethod);
    return customerSettings;
  }

  private PaymentSettingDto initPaymentSettingDto() {
    final PaymentSettingDto paymentSetting = new PaymentSettingDto();
    PaymentMethodDto paymentMethodDto = new PaymentMethodDto();
    paymentMethodDto.setDescCode(CREDIT);
    paymentMethodDto.setAllowChoose(true);
    paymentSetting.setPaymentMethods(Lists.newArrayList(paymentMethodDto));
    return paymentSetting;
  }

  private VUserDetail initVUserDetail(final int userSettingId) {
    final VUserDetail vUserDetail = new VUserDetail();
    vUserDetail.setUserSettingId(userSettingId);
    return vUserDetail;
  }

  private UserInfo initFinalCustomerUserInfo() {
    UserInfo user = new UserInfo();
    user.setRoles(Lists.newArrayList(EshopAuthority.FINAL_NORMAL_USER.name()));
    return user;
  }
}
