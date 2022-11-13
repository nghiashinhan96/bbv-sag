package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.EshopGroup;
import com.sagag.services.copydb.domain.dest.DestEshopGroup;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class EshopGroupProcessor implements ItemProcessor<EshopGroup, DestEshopGroup> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestEshopGroup process(EshopGroup item) throws Exception {
    return dozerBeanMapper.map(item, DestEshopGroup.class);
  }
}
