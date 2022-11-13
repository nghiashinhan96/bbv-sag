package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.CustomerSettings;
import com.sagag.services.copydb.domain.dest.DestCustomerSettings;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class CustomerSettingsProcessor implements ItemProcessor<CustomerSettings, DestCustomerSettings> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestCustomerSettings process(CustomerSettings item) throws Exception {
    return dozerBeanMapper.map(item, DestCustomerSettings.class);
  }
}
