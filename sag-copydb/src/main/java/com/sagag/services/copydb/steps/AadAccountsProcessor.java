package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.AadAccounts;
import com.sagag.services.copydb.domain.dest.DestAadAccounts;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class AadAccountsProcessor implements ItemProcessor<AadAccounts, DestAadAccounts> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestAadAccounts process(AadAccounts item) throws Exception {
    return dozerBeanMapper.map(item, DestAadAccounts.class);
  }
}
