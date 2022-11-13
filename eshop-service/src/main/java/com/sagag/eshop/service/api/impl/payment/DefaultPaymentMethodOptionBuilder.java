package com.sagag.eshop.service.api.impl.payment;

import com.sagag.eshop.repo.api.PaymentMethodRepository;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.domain.eshop.dto.PaymentMethodDto;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class DefaultPaymentMethodOptionBuilder implements PaymentMethodOptionBuilder {

  @Autowired
  private PaymentMethodRepository paymentRepo;

  @Override
  public List<PaymentMethodDto> build(boolean isSalesOnbehalfUser) {

    final List<String> filterPaymentMethodValues;
    if (isSalesOnbehalfUser) {
      filterPaymentMethodValues = PaymentMethodType.getPaymentMethodsForSales();
    } else {
      filterPaymentMethodValues = PaymentMethodType.getPaymentMethodsForB2BUsers();
    }

    return paymentRepo.findAllOrderByOrderDisplayAsc().stream()
        .filter(entity -> filterPaymentMethodValues.contains(entity.getDescCode()))
        .map(p -> PaymentMethodDto.builder().id(p.getId()).descCode(p.getDescCode())
            .description(p.getDescription()).payMethod(p.getPayMethod()).allowChoose(true).build())
        .collect(Collectors.toList());
  }

}
