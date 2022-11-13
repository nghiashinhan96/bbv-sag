package com.sagag.services.incentive.api.impl;

import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.incentive.api.IncentiveService;
import com.sagag.services.incentive.authcookie.CookiePrivacyException;
import com.sagag.services.incentive.config.IncentiveProfile;
import com.sagag.services.incentive.domain.IncentiveLoginDto;
import com.sagag.services.incentive.linkgenerator.IncentiveLinkGenerator;
import com.sagag.services.incentive.linkgenerator.impl.IncentiveMode;
import com.sagag.services.incentive.response.IncentiveLinkResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service
@IncentiveProfile
public class IncentiveServiceImpl implements IncentiveService {

  @Autowired
  private List<IncentiveLinkGenerator> incentiveLinkGenerators;

  @Override
  public IncentiveLinkResponse getIncentiveUrl(SupportedAffiliate affiliate, boolean showHappyPoints,
      Supplier<?>... suppliers) throws CookiePrivacyException {
    final IncentiveLinkResponse response = new IncentiveLinkResponse();

    IncentiveMode mode;
    for (IncentiveLinkGenerator linkGenerator : incentiveLinkGenerators) {
      mode = linkGenerator.support(affiliate, showHappyPoints);
      if (mode == null) {
        continue;
      }
      response.setMode(mode);

      final IncentiveLoginDto incentiveLogin = linkGenerator.buildLogin(suppliers);
      response.setUrl(linkGenerator.generate(incentiveLogin));
    }
    return response;
  }

}
