package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.EshopClientResource;
import com.sagag.services.copydb.domain.dest.DestEshopClientResource;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class EshopClientResourceProcessor implements ItemProcessor<EshopClientResource, DestEshopClientResource> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestEshopClientResource process(EshopClientResource item) throws Exception {
    return dozerBeanMapper.map(item, DestEshopClientResource.class);
  }
}
