package com.sagag.services.service.payment;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.ax.enums.wint.PaymentMethodAllowed;
import com.sagag.services.ax.enums.wint.WtPaymentType;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.eshop.dto.PaymentMethodDto;
import com.sagag.services.domain.eshop.dto.PaymentSettingDto;
import com.sagag.services.domain.sag.external.GrantedBranch;
import com.sagag.services.service.DataProvider;
import com.sagag.services.service.order.location.CourierBuilder;
import com.sagag.services.service.order.location.OrderLocationBuilder;
import com.sagag.services.service.user.handler.SbEshopSelfManageDataHandler;

import org.apache.commons.collections4.CollectionUtils;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class SbEshopSelfManageDataHandlerTest {

  @InjectMocks
  private SbEshopSelfManageDataHandler sbEshopSelfManageDataHandler;

  @Mock
  private UserPaymentSettingsFormBuilder userPaymentSettingsFormBuilder;

  @Mock
  private OrderLocationBuilder orderLocationBuilder;

  @Mock
  private CourierBuilder courierBuilder;

  @Test
  public void givenUserShouldReturnPaymentMethodDto() throws Exception {
    final UserInfo user = DataProvider.createUserInfo();
    user.setCompanyName(SupportedAffiliate.WBB.getCompanyName());
    user.getCustomer().setGrantedBranches(mockGrantedBranch());

    Mockito.when(
        userPaymentSettingsFormBuilder.buildUserPaymentSetting(user.getId(), user.isSaleOnBehalf()))
        .thenReturn(mockPaymentSettingDto());
    PaymentSettingDto result = sbEshopSelfManageDataHandler.getPaymentSetting(user);
    assertNotNull(result.getOrderLocations());
    assertThat(result.getOrderLocations().size(), Matchers.greaterThanOrEqualTo(0));
    if (!CollectionUtils.isEmpty(result.getOrderLocations())) {
      assertNotNull(result.getOrderLocations().get(0).getPaymentMethods());
      assertThat(result.getOrderLocations().get(0).getPaymentMethods().get(0).getDescCode(),
          Matchers.equalTo(WtPaymentType.CASH.name()));
    }
  }


  private List<GrantedBranch> mockGrantedBranch() {
    GrantedBranch grantedBranch = new GrantedBranch();
    grantedBranch.setBranchId("1000");
    grantedBranch.setPaymentMethodAllowed(PaymentMethodAllowed.CASH_ONLY.getCode());

    List<GrantedBranch> grantedBranches = new ArrayList<>();

    grantedBranches.add(grantedBranch);
    return grantedBranches;
  }

  private PaymentSettingDto mockPaymentSettingDto() {
    PaymentSettingDto paymentSettingDto = new PaymentSettingDto();
    PaymentMethodDto paymentMethod = new PaymentMethodDto();
    paymentMethod.setAllowChoose(false);
    paymentMethod.setDescCode("CASH");
    paymentMethod.setPayMethod("CASH");

    List<PaymentMethodDto> paymentMethods = new ArrayList<>();
    paymentMethods.add(paymentMethod);
    paymentSettingDto.setPaymentMethods(paymentMethods);
    return paymentSettingDto;
  }
}
