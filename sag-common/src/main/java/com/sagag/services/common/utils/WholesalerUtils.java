package com.sagag.services.common.utils;

import com.sagag.services.common.enums.SupportedAffiliate;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

@UtilityClass
@Slf4j
public class WholesalerUtils {

  private static final String EH_AFFILIATE_PATTERN_PREFIX = "eh-";

  public static final String EH_CH = EH_AFFILIATE_PATTERN_PREFIX + "ch";

  public static final String EH_AT = EH_AFFILIATE_PATTERN_PREFIX + "at";

  public static final String EH_CZ = EH_AFFILIATE_PATTERN_PREFIX + "cz";

  public boolean isFinalCustomerEndpoint(String affiliate) {
    log.debug("Checking the affiliate is wholesaler affiliate by affiliate = {}", affiliate);
    if (StringUtils.isBlank(affiliate)) {
      return false;
    }
    return StringUtils.startsWith(affiliate, EH_AFFILIATE_PATTERN_PREFIX);
  }

  public boolean isFinalCustomer(String affiliate, String orgCode) {
    return isFinalCustomerEndpoint(affiliate) && isFinalCustomer(orgCode);
  }

  public boolean isFinalCustomer(String orgCode) {
    return StringUtils.isBlank(orgCode);
  }

  public Optional<String> getWholesalerPortalNameByAffiliate(SupportedAffiliate affiliate) {
    if (affiliate == null) {
      return Optional.empty();
    }
    if (affiliate.isAtAffiliate()) {
      return Optional.of(EH_AT);
    }
    if (affiliate.isChAffiliate()) {
      return Optional.of(EH_CH);
    }
    if (affiliate.isCzAffiliate()) {
      return Optional.of(EH_CZ);
    }
    return Optional.empty();
  }

}
