package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.MessageLocationType;
import com.sagag.services.copydb.domain.dest.DestMessageLocationType;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class MessageLocationTypeProcessor implements ItemProcessor<MessageLocationType, DestMessageLocationType> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestMessageLocationType process(MessageLocationType item) throws Exception {
    return dozerBeanMapper.map(item, DestMessageLocationType.class);
  }
}
