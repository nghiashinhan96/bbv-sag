package com.sagag.services.hazelcast.api;

import com.hazelcast.core.IMap;
import com.sagag.services.elasticsearch.domain.SupplierDoc;
import com.sagag.services.elasticsearch.domain.SupplierTxt;
import com.sagag.services.hazelcast.app.HazelcastMaps;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Interface to define services for supplier caching.
 */
public interface SupplierCacheService extends CacheService {

  /**
   * Returns the map of {@link SupplierTxt} from its id.
   *
   * @param supplierIds the searching supplier ids
   * @return a map of {@link SupplierTxt}. Returns empty map if no supplier found.
   */
  Map<String, SupplierTxt> searchSupplierByIds(final List<String> supplierIds);

  /**
   * Returns the map of supplier name and its id.
   *
   * @param supplierIds the searching supplier ids
   * @return a map of supplier name and its id
   */
  Map<String, String> searchSupplierNameByIds(final List<String> supplierIds);

  /**
   * Refreshes the suppliers on the cache.
   *
   * @return the {@link IMap}
   */
  IMap<String, SupplierDoc> refreshCacheSuppliers(List<SupplierDoc> suppliers);


  /**
   * Returns all brand.
   *
   * @return the {@link List<SupplierTxt>} brand
   */
  List<SupplierTxt> findAllBrand();

  /**
   * Returns suppliers mapping by input suppname.
   *
   * @return the {@link List<SupplierTxt>} brand
   */
  Page<SupplierTxt> findLikeByName(String searchSuppName, Pageable pageable);

  @Override
  default String defName() {
    return HazelcastMaps.SUPPLIER_MAP.name();
  }

}
