package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.MessageAccessRightArea;
import com.sagag.services.copydb.domain.dest.DestMessageAccessRightArea;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class MessageAccessRightAreaProcessor implements ItemProcessor<MessageAccessRightArea, DestMessageAccessRightArea> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestMessageAccessRightArea process(MessageAccessRightArea item) throws Exception {
    return dozerBeanMapper.map(item, DestMessageAccessRightArea.class);
  }
}
