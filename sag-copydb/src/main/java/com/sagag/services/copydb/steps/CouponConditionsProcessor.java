package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.CouponConditions;
import com.sagag.services.copydb.domain.dest.DestCouponConditions;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class CouponConditionsProcessor implements ItemProcessor<CouponConditions, DestCouponConditions> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestCouponConditions process(CouponConditions item) throws Exception {
    return dozerBeanMapper.map(item, DestCouponConditions.class);
  }
}
