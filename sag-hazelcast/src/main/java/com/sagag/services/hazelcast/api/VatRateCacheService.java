package com.sagag.services.hazelcast.api;

import com.hazelcast.core.IMap;
import com.sagag.eshop.service.dto.VatRateDto;

import java.util.List;
import java.util.Optional;

/**
 * Interface to define services for vat rate caching.
 */

public interface VatRateCacheService {
  /**
   * Returns a map containing all vat rate.
   *
   * @return all vat rate
   */
  IMap<Integer, VatRateDto> getAllCacheVatRate();


  /**
   * Returns vat rate of this article
   * 
   * @param articleId
   * @return vat rate
   */
  Optional<Double> getCacheVatRateByArticleId(String articleId);

  /**
   * Returns list vat rate existed of this articles  
   * 
   * @param articleId
   * @return List VatRateDto
   */
   List<VatRateDto> getCacheVatRateByArticleIds(List<String> artIds);
  
  /**
   * Returns a map containing all refresh vat rate.
   *
   * @param vatRates vat rate to refresh
   * @return all cache refresh vat rate
   */
  IMap<Integer, VatRateDto> refreshAllCacheVatRate(List<VatRateDto> vatRates);
}
