package com.sagag.services.thule.config;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import com.sagag.services.thule.api.ThuleService;
import com.sagag.services.thule.domain.BuyersGuideData;
import com.sagag.services.thule.domain.ThuleProperties;

@Configuration
public class ThuleConfiguration {

  @Bean
  @ThuleProfile
  @ConfigurationProperties(prefix = "external.webservice.thule")
  public ThuleProperties thuleProperies() {
    return new ThuleProperties();
  }

  @Bean
  @ConditionalOnMissingBean(ThuleProperties.class)
  @Description("Initializing when disabling Thule feature.")
  public ThuleProperties defaultThuleProperties() {
    return new ThuleProperties();
  }

  @Bean
  @ConditionalOnMissingBean(ThuleService.class)
  public ThuleService defaultThuleServiceIfNotFoundBean() {
    return new ThuleService() {

      @Override
      public Optional<String> findThuleDealerUrlByAffiliate(String affiliate,
          boolean isSalesMode, Locale locale) {
        return Optional.empty();
      }

      @Override
      public Optional<BuyersGuideData> addBuyersGuide(Map<String, String> formData) {
        return Optional.empty();
      }
    };
  }

}
