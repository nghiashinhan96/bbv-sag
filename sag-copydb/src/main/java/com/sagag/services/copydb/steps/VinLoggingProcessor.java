package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.VinLogging;
import com.sagag.services.copydb.domain.dest.DestVinLogging;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class VinLoggingProcessor implements ItemProcessor<VinLogging, DestVinLogging> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestVinLogging process(VinLogging item) throws Exception {
    return dozerBeanMapper.map(item, DestVinLogging.class);
  }
}
