package com.sagag.services.article.api.attachedarticle;

import com.sagag.services.common.country.CountryConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultSupportedAttachedArticle implements SupportedAttachedArticle {

  @Autowired
  private CountryConfiguration countryConfig;

  @Override
  public boolean supportDepot() {
    return countryConfig.getSupportedAttachedArticle().isDepot();
  }

  @Override
  public boolean supportRecycle() {
    return countryConfig.getSupportedAttachedArticle().isRecycle();
  }

  @Override
  public boolean supportVoc() {
    return countryConfig.getSupportedAttachedArticle().isVoc();
  }

  @Override
  public boolean supportVrg() {
    return countryConfig.getSupportedAttachedArticle().isVrg();
  }

}
