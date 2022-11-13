package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.OfferPersonProperty;
import com.sagag.services.copydb.domain.dest.DestOfferPersonProperty;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class OfferPersonPropertyProcessor implements ItemProcessor<OfferPersonProperty, DestOfferPersonProperty> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestOfferPersonProperty process(OfferPersonProperty item) throws Exception {
    return dozerBeanMapper.map(item, DestOfferPersonProperty.class);
  }
}
