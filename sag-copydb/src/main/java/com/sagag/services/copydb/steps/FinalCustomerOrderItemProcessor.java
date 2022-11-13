package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.FinalCustomerOrderItem;
import com.sagag.services.copydb.domain.dest.DestFinalCustomerOrderItem;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class FinalCustomerOrderItemProcessor implements ItemProcessor<FinalCustomerOrderItem, DestFinalCustomerOrderItem> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestFinalCustomerOrderItem process(FinalCustomerOrderItem item) throws Exception {
    return dozerBeanMapper.map(item, DestFinalCustomerOrderItem.class);
  }
}
