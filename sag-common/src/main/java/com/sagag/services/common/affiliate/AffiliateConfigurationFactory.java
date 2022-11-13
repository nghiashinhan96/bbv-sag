package com.sagag.services.common.affiliate;

import com.sagag.services.common.enums.HashType;

public interface AffiliateConfigurationFactory {

  /**
   * Returns hash type by country.
   *
   */
  HashType getHashType();

}
