package com.sagag.eshop.service.affiliate.impl;

import com.sagag.eshop.service.affiliate.SupportedAffiliatePredicate;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.profiles.SbProfile;
import com.sagag.services.domain.eshop.message.dto.SupportedAffiliateDto;

import org.springframework.stereotype.Component;

@Component
@SbProfile
public class SbSupportedAffiliatePredicateImpl implements SupportedAffiliatePredicate {

  @Override
  public boolean test(SupportedAffiliateDto item) {
    return SupportedAffiliate.fromCompanyName(item.getCompanyName()).isSbAffiliate();
  }

}
