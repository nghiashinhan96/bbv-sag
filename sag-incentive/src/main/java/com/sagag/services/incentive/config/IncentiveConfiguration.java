package com.sagag.services.incentive.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.incentive.api.IncentiveService;
import com.sagag.services.incentive.authcookie.CookiePrivacyException;
import com.sagag.services.incentive.response.IncentiveLinkResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.function.Supplier;

/**
 * Configuration for incentive component.
 */
@Configuration
public class IncentiveConfiguration {

  @Autowired
  private ClientHttpRequestFactory httpRequestFactory;

  @Bean
  @IncentiveProfile
  public RestTemplate incentiveRestTemplate() {
    final RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
    final List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
    final ObjectMapper objectMapper = new ObjectMapper();
    for (HttpMessageConverter<?> converter : converters) {
      if (converter instanceof MappingJackson2HttpMessageConverter) {
        MappingJackson2HttpMessageConverter jsonConverter =
            (MappingJackson2HttpMessageConverter) converter;
        jsonConverter.setObjectMapper(objectMapper);
      }
    }
    return restTemplate;
  }

  @Bean
  @ConditionalOnMissingBean(IncentiveService.class)
  public IncentiveService defaultIncentiveService() {
    return new IncentiveService() {

      @Override
      public IncentiveLinkResponse getIncentiveUrl(SupportedAffiliate affiliate,
          boolean showHappyPoints, Supplier<?>... suppliers) throws CookiePrivacyException {
        throw new UnsupportedOperationException();
      }
    };
  }
}
