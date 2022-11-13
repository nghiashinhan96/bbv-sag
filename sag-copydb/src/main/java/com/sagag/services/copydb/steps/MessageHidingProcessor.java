package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.MessageHiding;
import com.sagag.services.copydb.domain.dest.DestMessageHiding;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class MessageHidingProcessor implements ItemProcessor<MessageHiding, DestMessageHiding> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestMessageHiding process(MessageHiding item) throws Exception {
    return dozerBeanMapper.map(item, DestMessageHiding.class);
  }
}
