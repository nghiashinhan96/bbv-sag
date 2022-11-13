package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.EshopPermission;
import com.sagag.services.copydb.domain.dest.DestEshopPermission;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class EshopPermissionProcessor implements ItemProcessor<EshopPermission, DestEshopPermission> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestEshopPermission process(EshopPermission item) throws Exception {
    return dozerBeanMapper.map(item, DestEshopPermission.class);
  }
}
