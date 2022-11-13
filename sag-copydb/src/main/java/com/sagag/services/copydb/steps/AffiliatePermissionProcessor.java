package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.AffiliatePermission;
import com.sagag.services.copydb.domain.dest.DestAffiliatePermission;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class AffiliatePermissionProcessor implements ItemProcessor<AffiliatePermission, DestAffiliatePermission> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestAffiliatePermission process(AffiliatePermission item) throws Exception {
    return dozerBeanMapper.map(item, DestAffiliatePermission.class);
  }
}
