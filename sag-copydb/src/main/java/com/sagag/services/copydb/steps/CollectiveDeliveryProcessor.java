package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.CollectiveDelivery;
import com.sagag.services.copydb.domain.dest.DestCollectiveDelivery;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class CollectiveDeliveryProcessor implements ItemProcessor<CollectiveDelivery, DestCollectiveDelivery> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestCollectiveDelivery process(CollectiveDelivery item) throws Exception {
    return dozerBeanMapper.map(item, DestCollectiveDelivery.class);
  }
}
