package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.OfferPerson;
import com.sagag.services.copydb.domain.dest.DestOfferPerson;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class OfferPersonProcessor implements ItemProcessor<OfferPerson, DestOfferPerson> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestOfferPerson process(OfferPerson item) throws Exception {
    return dozerBeanMapper.map(item, DestOfferPerson.class);
  }
}
