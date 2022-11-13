package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.MessageLocationTypeRoleType;
import com.sagag.services.copydb.domain.dest.DestMessageLocationTypeRoleType;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class MessageLocationTypeRoleTypeProcessor implements ItemProcessor<MessageLocationTypeRoleType, DestMessageLocationTypeRoleType> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestMessageLocationTypeRoleType process(MessageLocationTypeRoleType item) throws Exception {
    return dozerBeanMapper.map(item, DestMessageLocationTypeRoleType.class);
  }
}
