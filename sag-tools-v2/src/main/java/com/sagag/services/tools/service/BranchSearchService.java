package com.sagag.services.tools.service;

import com.sagag.services.tools.domain.elasticsearch.BranchDoc;
import com.sagag.services.tools.support.elasticsearch.indices.BranchIndexLang;
import com.sagag.services.tools.support.elasticsearch.indices.IMultilingualIndex;

import java.util.Optional;

/**
 * Elasticsearch service interfacing for branch document.
 */
public interface BranchSearchService extends IMultilingualIndex {

  /**
   * Searchs branch doc by branch number.
   *
   * @param branchNr branch number
   * @param indexBranchES 
   *
   * @return {@link BranchDoc}
   */
  Optional<BranchDoc> searchByBranchNr(final Integer branchNr, String indexBranchES);

  @Override
  default String index() {
    return findIndex(BranchIndexLang.values());
  }
}
