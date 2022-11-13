package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.AllocationType;
import com.sagag.services.copydb.domain.dest.DestAllocationType;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class AllocationTypeProcessor implements ItemProcessor<AllocationType, DestAllocationType> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestAllocationType process(AllocationType item) throws Exception {
    return dozerBeanMapper.map(item, DestAllocationType.class);
  }
}
