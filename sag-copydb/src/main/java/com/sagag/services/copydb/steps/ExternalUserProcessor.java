package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.ExternalUser;
import com.sagag.services.copydb.domain.dest.DestExternalUser;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class ExternalUserProcessor implements ItemProcessor<ExternalUser, DestExternalUser> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestExternalUser process(ExternalUser item) throws Exception {
    return dozerBeanMapper.map(item, DestExternalUser.class);
  }
}
