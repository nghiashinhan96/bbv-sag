package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.MessageSubArea;
import com.sagag.services.copydb.domain.dest.DestMessageSubArea;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class MessageSubAreaProcessor implements ItemProcessor<MessageSubArea, DestMessageSubArea> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestMessageSubArea process(MessageSubArea item) throws Exception {
    return dozerBeanMapper.map(item, DestMessageSubArea.class);
  }
}
