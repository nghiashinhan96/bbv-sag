package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.MessageLanguage;
import com.sagag.services.copydb.domain.dest.DestMessageLanguage;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class MessageLanguageProcessor implements ItemProcessor<MessageLanguage, DestMessageLanguage> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestMessageLanguage process(MessageLanguage item) throws Exception {
    return dozerBeanMapper.map(item, DestMessageLanguage.class);
  }
}