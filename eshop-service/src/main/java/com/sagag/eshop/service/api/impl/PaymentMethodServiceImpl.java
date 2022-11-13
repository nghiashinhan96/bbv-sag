package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.PaymentMethodRepository;
import com.sagag.eshop.repo.entity.PaymentMethod;
import com.sagag.eshop.service.api.PaymentMethodService;
import com.sagag.eshop.service.api.impl.payment.PaymentMethodOptionBuilder;
import com.sagag.services.domain.eshop.dto.PaymentMethodDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

  @Autowired
  private PaymentMethodRepository paymentMethodRepo;

  @Autowired
  private PaymentMethodOptionBuilder paymentMethodOptionBuilder;

  @Override
  public Optional<PaymentMethod> getPaymentMethodById(Integer id) {
    return paymentMethodRepo.findById(id);
  }

  @Override
  public List<PaymentMethodDto> getPaymentMethodOptions(boolean isSalesOnbehalfUser) {
    return paymentMethodOptionBuilder.build(isSalesOnbehalfUser);
  }
}
