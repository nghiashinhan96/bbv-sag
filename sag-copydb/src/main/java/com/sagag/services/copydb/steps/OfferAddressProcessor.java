package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.OfferAddress;
import com.sagag.services.copydb.domain.dest.DestOfferAddress;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class OfferAddressProcessor implements ItemProcessor<OfferAddress, DestOfferAddress> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestOfferAddress process(OfferAddress item) throws Exception {
    return dozerBeanMapper.map(item, DestOfferAddress.class);
  }
}
