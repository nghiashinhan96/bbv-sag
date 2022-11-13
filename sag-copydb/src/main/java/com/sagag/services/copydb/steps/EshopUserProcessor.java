package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.EshopUser;
import com.sagag.services.copydb.domain.dest.DestEshopUser;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class EshopUserProcessor implements ItemProcessor<EshopUser, DestEshopUser> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestEshopUser process(EshopUser item) throws Exception {
    return dozerBeanMapper.map(item, DestEshopUser.class);
  }
}
