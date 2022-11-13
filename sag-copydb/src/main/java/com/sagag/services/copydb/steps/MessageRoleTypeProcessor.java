package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.MessageRoleType;
import com.sagag.services.copydb.domain.dest.DestMessageRoleType;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class MessageRoleTypeProcessor implements ItemProcessor<MessageRoleType, DestMessageRoleType> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestMessageRoleType process(MessageRoleType item) throws Exception {
    return dozerBeanMapper.map(item, DestMessageRoleType.class);
  }
}
