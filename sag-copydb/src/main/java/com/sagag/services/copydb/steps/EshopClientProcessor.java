package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.EshopClient;
import com.sagag.services.copydb.domain.dest.DestEshopClient;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class EshopClientProcessor implements ItemProcessor<EshopClient, DestEshopClient> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestEshopClient process(EshopClient item) throws Exception {
    return dozerBeanMapper.map(item, DestEshopClient.class);
  }
}
