package com.sagag.services.thule.api;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import com.sagag.services.thule.domain.BuyersGuideData;

public interface ThuleService {

  /**
   * Adds buyers guile from Thule system response.
   *
   * @param formData the Thule data
   * @return the optional of extracted data
   */
  Optional<BuyersGuideData> addBuyersGuide(Map<String, String> formData);

  /**
   * Returns the Thule dealer URL by affiliate and locale.
   *
   * @param affiliate the requested affiliate
   * @param isSalesMode the flag of sales mode
   * @param locale the user locale
   * @return the optional of Thule dealer URL if exists.
   */
  Optional<String> findThuleDealerUrlByAffiliate(String affiliate, boolean isSalesMode,
      Locale locale);

}
