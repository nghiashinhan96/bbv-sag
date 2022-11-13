package com.sagag.eshop.service.affiliate.impl;

import com.sagag.eshop.service.affiliate.SupportedAffiliatePredicate;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.profiles.AtProfile;
import com.sagag.services.domain.eshop.message.dto.SupportedAffiliateDto;

import org.springframework.stereotype.Component;

@Component
@AtProfile
public class AtSupportedAffiliatePredicateImpl implements SupportedAffiliatePredicate {

  @Override
  public boolean test(SupportedAffiliateDto item) {
    return SupportedAffiliate.fromCompanyName(item.getCompanyName()).isAtAffiliate();
  }

}
