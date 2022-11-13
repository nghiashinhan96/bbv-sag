package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.BusinessLog;
import com.sagag.services.copydb.domain.dest.DestBusinessLog;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class BusinessLogProcessor implements ItemProcessor<BusinessLog, DestBusinessLog> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestBusinessLog process(BusinessLog item) throws Exception {
    return dozerBeanMapper.map(item, DestBusinessLog.class);
  }
}
