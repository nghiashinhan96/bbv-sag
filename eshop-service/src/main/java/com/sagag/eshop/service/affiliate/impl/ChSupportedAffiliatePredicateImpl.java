package com.sagag.eshop.service.affiliate.impl;

import com.sagag.eshop.service.affiliate.SupportedAffiliatePredicate;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.profiles.ChProfile;
import com.sagag.services.domain.eshop.message.dto.SupportedAffiliateDto;

import org.springframework.stereotype.Component;

@Component
@ChProfile
public class ChSupportedAffiliatePredicateImpl implements SupportedAffiliatePredicate {

  @Override
  public boolean test(SupportedAffiliateDto item) {
    return SupportedAffiliate.fromCompanyName(item.getCompanyName()).isChAffiliate();
  }

}
