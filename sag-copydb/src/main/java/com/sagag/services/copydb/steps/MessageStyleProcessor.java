package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.MessageStyle;
import com.sagag.services.copydb.domain.dest.DestMessageStyle;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class MessageStyleProcessor implements ItemProcessor<MessageStyle, DestMessageStyle> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestMessageStyle process(MessageStyle item) throws Exception {
    return dozerBeanMapper.map(item, DestMessageStyle.class);
  }
}
