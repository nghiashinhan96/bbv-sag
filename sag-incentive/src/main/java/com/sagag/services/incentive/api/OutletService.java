package com.sagag.services.incentive.api;

import com.sagag.services.common.enums.SupportedAffiliate;

public interface OutletService {

  /**
   * Returns outlet URL by affiliate.
   *
   * @param affiliate the supported affiliate
   * @param lang the user language
   * @param email the user email
   * @param username the user name
   * @param custNr the customer of user
   * @return the result of outlet URL.
   */
  String getOutletUrl(SupportedAffiliate affiliate, String lang, String email, String username,
      Long custNr);

}
