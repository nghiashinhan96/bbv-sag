package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.CouponUseLog;
import com.sagag.services.copydb.domain.dest.DestCouponUseLog;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class CouponUseLogProcessor implements ItemProcessor<CouponUseLog, DestCouponUseLog> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestCouponUseLog process(CouponUseLog item) throws Exception {
    return dozerBeanMapper.map(item, DestCouponUseLog.class);
  }
}
