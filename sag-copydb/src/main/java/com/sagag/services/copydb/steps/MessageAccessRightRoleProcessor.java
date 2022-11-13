package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.MessageAccessRightRole;
import com.sagag.services.copydb.domain.dest.DestMessageAccessRightRole;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class MessageAccessRightRoleProcessor implements ItemProcessor<MessageAccessRightRole, DestMessageAccessRightRole> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestMessageAccessRightRole process(MessageAccessRightRole item) throws Exception {
    return dozerBeanMapper.map(item, DestMessageAccessRightRole.class);
  }
}
