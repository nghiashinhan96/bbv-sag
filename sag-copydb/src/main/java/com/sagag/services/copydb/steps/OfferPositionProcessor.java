package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.OfferPosition;
import com.sagag.services.copydb.domain.dest.DestOfferPosition;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class OfferPositionProcessor implements ItemProcessor<OfferPosition, DestOfferPosition> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestOfferPosition process(OfferPosition item) throws Exception {
    return dozerBeanMapper.map(item, DestOfferPosition.class);
  }
}
