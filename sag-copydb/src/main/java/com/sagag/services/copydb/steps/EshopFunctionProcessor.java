package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.EshopFunction;
import com.sagag.services.copydb.domain.dest.DestEshopFunction;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class EshopFunctionProcessor implements ItemProcessor<EshopFunction, DestEshopFunction> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestEshopFunction process(EshopFunction item) throws Exception {
    return dozerBeanMapper.map(item, DestEshopFunction.class);
  }
}
