package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.MessageLocation;
import com.sagag.services.copydb.domain.dest.DestMessageLocation;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class MessageLocationProcessor implements ItemProcessor<MessageLocation, DestMessageLocation> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestMessageLocation process(MessageLocation item) throws Exception {
    return dozerBeanMapper.map(item, DestMessageLocation.class);
  }
}
