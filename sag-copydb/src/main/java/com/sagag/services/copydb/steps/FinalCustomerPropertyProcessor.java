package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.FinalCustomerProperty;
import com.sagag.services.copydb.domain.dest.DestFinalCustomerProperty;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class FinalCustomerPropertyProcessor implements ItemProcessor<FinalCustomerProperty, DestFinalCustomerProperty> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestFinalCustomerProperty process(FinalCustomerProperty item) throws Exception {
    return dozerBeanMapper.map(item, DestFinalCustomerProperty.class);
  }
}
