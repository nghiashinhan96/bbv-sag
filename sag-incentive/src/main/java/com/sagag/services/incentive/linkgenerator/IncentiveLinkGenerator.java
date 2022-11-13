package com.sagag.services.incentive.linkgenerator;

import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.incentive.authcookie.CookiePrivacyException;
import com.sagag.services.incentive.domain.IncentiveLoginDto;
import com.sagag.services.incentive.linkgenerator.impl.IncentiveMode;

import java.util.function.Supplier;

public interface IncentiveLinkGenerator {

  /**
   * Generates the incentive URL.
   *
   */
  String generate(IncentiveLoginDto login) throws CookiePrivacyException;

  /**
   * Verifies this settings is correct for generating URL.
   *
   */
  IncentiveMode support(SupportedAffiliate affiliate, boolean showHappyPoints);

  /**
   * Builds incentive login info.
   *
   */
  IncentiveLoginDto buildLogin(Supplier<?>... suppliers);

  /**
   * Returns the customer number value from given supplier.
   *
   * @param customerSupplier
   * @return the customer number with <code>String</code> format
   */
  default String getCustomerNr(Supplier<?> customerSupplier) {
    return (String) customerSupplier.get();
  }
}
