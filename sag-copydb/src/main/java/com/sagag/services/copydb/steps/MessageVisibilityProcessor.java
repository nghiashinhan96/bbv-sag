package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.MessageVisibility;
import com.sagag.services.copydb.domain.dest.DestMessageVisibility;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class MessageVisibilityProcessor implements ItemProcessor<MessageVisibility, DestMessageVisibility> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestMessageVisibility process(MessageVisibility item) throws Exception {
    return dozerBeanMapper.map(item, DestMessageVisibility.class);
  }
}
