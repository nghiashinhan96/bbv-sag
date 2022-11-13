package com.sagag.services.service.incentive;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.incentive.authcookie.CookiePrivacyException;
import com.sagag.services.incentive.response.IncentiveLinkResponse;

public interface CompositeIncentivePointLinkGenerator {

  IncentiveLinkResponse generate(UserInfo user) throws CookiePrivacyException;

}
