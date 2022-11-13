package com.sagag.services.incentive.api;

import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.incentive.authcookie.CookiePrivacyException;
import com.sagag.services.incentive.response.IncentiveLinkResponse;

import java.util.function.Supplier;

public interface IncentiveService {

  /**
   * Returns the incentive URL of affiliates.
   *
   * @param affiliate the selected affiliate
   * @param showHappyPoints the settings to show happy points flag
   * @param suppliers the any suppliers for generating token
   * @return the incentive response object
   */
  IncentiveLinkResponse getIncentiveUrl(SupportedAffiliate affiliate, boolean showHappyPoints,
      Supplier<?>... suppliers) throws CookiePrivacyException;

}
