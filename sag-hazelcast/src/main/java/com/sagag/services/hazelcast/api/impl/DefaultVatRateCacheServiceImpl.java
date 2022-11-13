package com.sagag.services.hazelcast.api.impl;

import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicates;
import com.sagag.eshop.service.dto.VatRateDto;
import com.sagag.services.common.profiles.EnablePriceDiscountPromotion;
import com.sagag.services.hazelcast.app.HazelcastMaps;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Cache service implementation class for vat rate.
 */
@Service
@Slf4j
@EnablePriceDiscountPromotion(true)
public class DefaultVatRateCacheServiceImpl extends AbstractVatRateCacheService {

  private static final String ART_ID = "artId";

  private static final String MAP_NAME = HazelcastMaps.VAT_RATE_MAP.name();

  @Override
  public IMap<Integer, VatRateDto> getAllCacheVatRate() {
    log.debug("Getting all vat rate from cache");
    return hazelcastInstance.getMap(MAP_NAME);
  }

  @Override
  public Optional<Double> getCacheVatRateByArticleId(String articleId) {
    final IMap<Integer, VatRateDto> vatRates = getAllCacheVatRate();
    return vatRates.values(Predicates.equal(ART_ID, articleId)).stream().findFirst()
        .map(VatRateDto::getCustomVatRate);
  }

  @Override
  public List<VatRateDto> getCacheVatRateByArticleIds(List<String> artIds) {
    final IMap<Integer, VatRateDto> vatRates = getAllCacheVatRate();
    return vatRates.values(Predicates.in(ART_ID, artIds.toArray(new String[artIds.size()])))
        .stream().collect(Collectors.toList());
  }

  @Override
  public IMap<Integer, VatRateDto> refreshAllCacheVatRate(List<VatRateDto> vatRates) {
    log.info("Caching all vat rate from DB to Hazelcast instance");
    final IMap<Integer, VatRateDto> vatRateMap = getAllCacheVatRate();
    vatRates.parallelStream().forEach(vat -> vatRateMap.put(vat.getId(), vat));
    return vatRateMap;
  }

  @Override
  public void refreshCacheAll() {
    refreshAllCacheVatRate(vatRateService.getAll());
  }

}
