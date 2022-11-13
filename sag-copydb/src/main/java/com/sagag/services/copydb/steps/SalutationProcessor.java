package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.Salutation;
import com.sagag.services.copydb.domain.dest.DestSalutation;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class SalutationProcessor implements ItemProcessor<Salutation, DestSalutation> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestSalutation process(Salutation item) throws Exception {
    return dozerBeanMapper.map(item, DestSalutation.class);
  }
}
