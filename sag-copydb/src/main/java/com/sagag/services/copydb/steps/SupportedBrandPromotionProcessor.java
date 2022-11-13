package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.SupportedBrandPromotion;
import com.sagag.services.copydb.domain.dest.DestSupportedBrandPromotion;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class SupportedBrandPromotionProcessor implements ItemProcessor<SupportedBrandPromotion, DestSupportedBrandPromotion> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestSupportedBrandPromotion process(SupportedBrandPromotion item) throws Exception {
    return dozerBeanMapper.map(item, DestSupportedBrandPromotion.class);
  }
}
