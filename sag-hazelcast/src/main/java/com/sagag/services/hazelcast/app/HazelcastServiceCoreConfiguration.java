package com.sagag.services.hazelcast.app;

import com.hazelcast.core.IMap;
import com.sagag.eshop.service.dto.VatRateDto;
import com.sagag.services.hazelcast.api.impl.AbstractVatRateCacheService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Configuration
@Slf4j
public class HazelcastServiceCoreConfiguration {

  private static final String WARN_MSG = "No support cache Vat Rate for this System";

  @Bean
  @ConditionalOnMissingBean(AbstractVatRateCacheService.class)
  public AbstractVatRateCacheService defaultAbstractVatRateCacheService() {
    return new AbstractVatRateCacheService() {

      @Override
      public IMap<Integer, VatRateDto> getAllCacheVatRate() {
        log.debug(WARN_MSG);
        return hazelcastInstance.getMap(HazelcastMaps.VAT_RATE_MAP.name());
      }

      @Override
      public Optional<Double> getCacheVatRateByArticleId(String articleId) {
        return Optional.empty();
      }

      @Override
      public List<VatRateDto> getCacheVatRateByArticleIds(List<String> artIds) {
        return Collections.emptyList();
      }

      @Override
      public IMap<Integer, VatRateDto> refreshAllCacheVatRate(List<VatRateDto> vatRates) {
        log.debug(WARN_MSG);
        return getAllCacheVatRate();
      }

      @Override
      public void refreshCacheAll() {
        log.debug(WARN_MSG);
      }

    };
  }

}
