package com.sagag.services.service.order.location;

import com.sagag.services.ax.enums.wint.PaymentMethodAllowed;
import com.sagag.services.ax.enums.wint.WtPaymentType;
import com.sagag.services.common.profiles.WintProfile;
import com.sagag.services.domain.eshop.dto.CourierDto;
import com.sagag.services.domain.eshop.dto.DeliveryTypeDto;
import com.sagag.services.domain.eshop.dto.GrantedBranchDto;
import com.sagag.services.domain.eshop.dto.OrderLocation;
import com.sagag.services.domain.eshop.dto.PaymentMethodDto;
import com.sagag.services.domain.sag.external.GrantedBranch;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@WintProfile
public class OrderLocationBuilder {

  @Autowired
  private GrantedBranchDtoBuilder grantedBranchDtoBuilder;

  public List<OrderLocation> buildOrderLocations(List<GrantedBranch> grantedBranches,
      String compName, List<DeliveryTypeDto> deliveryTypes, List<PaymentMethodDto> paymentMethods,
      List<CourierDto> courierServices) {
    if (CollectionUtils.isEmpty(grantedBranches)) {
      return Collections.emptyList();
    }

    return grantedBranches.stream().map(branch -> buildOrderLocation(branch,
            deliveryTypes, paymentMethods, courierServices, compName))
        .collect(Collectors.toList());
  }

  public OrderLocation buildOrderLocation(GrantedBranch branch,
      List<DeliveryTypeDto> deliveryTypes, List<PaymentMethodDto> paymentMethods,
      List<CourierDto> courierServices, String companyName) {
    List<PaymentMethodDto> paymentMethodsByBranches = paymentMethods.stream()
        .filter(p -> allowPaymentMethod(branch, p)).collect(Collectors.toList());

    final GrantedBranchDto grantedBranchDto = grantedBranchDtoBuilder.from(branch, companyName);

    return OrderLocation.builder()
        .branch(grantedBranchDto)
        .deliveryTypes(deliveryTypes)
        .paymentMethods(paymentMethodsByBranches)
        .courierServices(courierServices)
        .build();
  }

  private boolean allowPaymentMethod(GrantedBranch branch, PaymentMethodDto paymentMethod) {
    final PaymentMethodAllowed paymentMethodAllowed =
        PaymentMethodAllowed.findByCode(branch.getPaymentMethodAllowed());

    WtPaymentType paymentType = WtPaymentType.findByCode(paymentMethod.getDescCode());

    return Arrays.asList(paymentMethodAllowed.getAllowsPayments()).contains(paymentType);
  }
}
