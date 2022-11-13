package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.EshopRole;
import com.sagag.services.copydb.domain.dest.DestEshopRole;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class EshopRoleProcessor implements ItemProcessor<EshopRole, DestEshopRole> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestEshopRole process(EshopRole item) throws Exception {
    return dozerBeanMapper.map(item, DestEshopRole.class);
  }
}
