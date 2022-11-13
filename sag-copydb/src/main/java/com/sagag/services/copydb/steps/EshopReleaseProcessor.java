package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.EshopRelease;
import com.sagag.services.copydb.domain.dest.DestEshopRelease;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class EshopReleaseProcessor implements ItemProcessor<EshopRelease, DestEshopRelease> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestEshopRelease process(EshopRelease item) throws Exception {
    return dozerBeanMapper.map(item, DestEshopRelease.class);
  }
}
