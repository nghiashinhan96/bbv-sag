package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.MessageArea;
import com.sagag.services.copydb.domain.dest.DestMessageArea;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class MessageAreaProcessor implements ItemProcessor<MessageArea, DestMessageArea> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestMessageArea process(MessageArea item) throws Exception {
    return dozerBeanMapper.map(item, DestMessageArea.class);
  }
}
