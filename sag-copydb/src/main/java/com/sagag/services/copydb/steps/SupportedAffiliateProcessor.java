package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.SupportedAffiliate;
import com.sagag.services.copydb.domain.dest.DestSupportedAffiliate;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class SupportedAffiliateProcessor implements ItemProcessor<SupportedAffiliate, DestSupportedAffiliate> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestSupportedAffiliate process(SupportedAffiliate item) throws Exception {
    return dozerBeanMapper.map(item, DestSupportedAffiliate.class);
  }
}
