package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.Languages;
import com.sagag.services.copydb.domain.dest.DestLanguages;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class LanguagesProcessor implements ItemProcessor<Languages, DestLanguages> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestLanguages process(Languages item) throws Exception {
    return dozerBeanMapper.map(item, DestLanguages.class);
  }
}
