package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.PermFunction;
import com.sagag.services.copydb.domain.dest.DestPermFunction;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class PermFunctionProcessor implements ItemProcessor<PermFunction, DestPermFunction> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestPermFunction process(PermFunction item) throws Exception {
    return dozerBeanMapper.map(item, DestPermFunction.class);
  }
}
