package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.entity.PaymentMethod;
import com.sagag.services.domain.eshop.dto.PaymentMethodDto;

import java.util.List;
import java.util.Optional;

public interface PaymentMethodService {

  /**
   * Returns {@link PaymentMethod} by id.
   *
   * @param id the id to search paymentMethod.
   * @return the null-able user.
   */
  Optional<PaymentMethod> getPaymentMethodById(Integer id);


  /**
   * Returns list of payment method options.
   *
   * @param salesOnBehalf
   * @return list {@link PaymentMethodDto}
   */
  List<PaymentMethodDto> getPaymentMethodOptions(boolean isSalesOnbehalfUser);

}
