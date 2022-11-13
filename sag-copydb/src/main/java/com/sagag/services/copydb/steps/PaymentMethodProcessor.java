package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.PaymentMethod;
import com.sagag.services.copydb.domain.dest.DestPaymentMethod;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class PaymentMethodProcessor implements ItemProcessor<PaymentMethod, DestPaymentMethod> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestPaymentMethod process(PaymentMethod item) throws Exception {
    return dozerBeanMapper.map(item, DestPaymentMethod.class);
  }
}
