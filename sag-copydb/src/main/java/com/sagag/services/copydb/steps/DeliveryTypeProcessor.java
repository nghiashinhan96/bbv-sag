package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.DeliveryType;
import com.sagag.services.copydb.domain.dest.DestDeliveryType;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class DeliveryTypeProcessor implements ItemProcessor<DeliveryType, DestDeliveryType> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestDeliveryType process(DeliveryType item) throws Exception {
    return dozerBeanMapper.map(item, DestDeliveryType.class);
  }
}
