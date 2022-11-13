package com.sagag.services.elasticsearch.api;

import com.sagag.services.elasticsearch.domain.SupplierDoc;

import java.util.List;

/**
 * Elasticsearch service for supplier.
 */
public interface SupplierSearchService extends IFetchAllDataService<SupplierDoc> {

  /**
   * Returns top 10 available suppliers.
   * <p>
   * only used for IT tests to reduce performance.
   *
   * @return a list of {@link SupplierDoc}.
   */
  List<SupplierDoc> getTop10Suppliers();
}
