package com.sagag.services.haynespro.config;

import com.sagag.services.haynespro.api.HaynesProService;
import com.sagag.services.haynespro.dto.HaynesProOptionDto;
import com.sagag.services.haynespro.dto.HaynesProShoppingCart;
import com.sagag.services.haynespro.request.HaynesProAccessUrlRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Configuration
public class HaynesProCoreConfiguration {

  @Bean
  @ConditionalOnMissingBean(HaynesProService.class)
  public HaynesProService defaultHaynesProService() {
    return new HaynesProService() {
      @Override
      public List<HaynesProOptionDto> getHaynesProAccessOptions() {
        return Collections.emptyList();
      }

      @Override
      public String getHaynesProAccessUrl(HaynesProAccessUrlRequest request) {
        return StringUtils.EMPTY;
      }

      @Override
      public Optional<HaynesProShoppingCart> getHaynesProShoppingCart(String key,
          BufferedReader reader) {
        return Optional.empty();
      }
    };
  }

  @Bean
  @ConditionalOnMissingBean(HaynesProInternalProperties.class)
  public HaynesProInternalProperties defaultHaynesProInternalProperties() {
    final HaynesProInternalProperties defaultProperties = new HaynesProInternalProperties();
    defaultProperties.setCallbackUrl(StringUtils.EMPTY);

    final HaynesProLicenseProperty licenseProperty = new HaynesProLicenseProperty();
    defaultProperties.setLicense(licenseProperty);
    return defaultProperties;
  }

}
