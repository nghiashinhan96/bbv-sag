package com.sagag.services.common.affiliate.impl;

import com.sagag.services.common.affiliate.AffiliateConfigurationFactory;
import com.sagag.services.common.country.CountryConfiguration;
import com.sagag.services.common.enums.HashType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AffiliateConfigurationFactoryImpl implements AffiliateConfigurationFactory {

  @Autowired
  private CountryConfiguration countryConfiguration;

  @Override
  public HashType getHashType() {
    return countryConfiguration.getHashType();
  }

}
