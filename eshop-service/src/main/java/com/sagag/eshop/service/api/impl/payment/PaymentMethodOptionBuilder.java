package com.sagag.eshop.service.api.impl.payment;

import com.sagag.services.domain.eshop.dto.PaymentMethodDto;

import java.util.List;

public interface PaymentMethodOptionBuilder {

  public List<PaymentMethodDto> build(boolean isSalesOnbehalfUser);

}
