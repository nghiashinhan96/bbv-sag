package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.OrderStatus;
import com.sagag.services.copydb.domain.dest.DestOrderStatus;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class OrderStatusProcessor implements ItemProcessor<OrderStatus, DestOrderStatus> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestOrderStatus process(OrderStatus item) throws Exception {
    return dozerBeanMapper.map(item, DestOrderStatus.class);
  }
}
