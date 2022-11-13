package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.MessageAccessRight;
import com.sagag.services.copydb.domain.dest.DestMessageAccessRight;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class MessageAccessRightProcessor implements ItemProcessor<MessageAccessRight, DestMessageAccessRight> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestMessageAccessRight process(MessageAccessRight item) throws Exception {
    return dozerBeanMapper.map(item, DestMessageAccessRight.class);
  }
}
