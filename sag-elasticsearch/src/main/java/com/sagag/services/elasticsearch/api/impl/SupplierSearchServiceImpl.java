package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.elasticsearch.api.AbstractElasticsearchService;
import com.sagag.services.elasticsearch.api.SupplierSearchService;
import com.sagag.services.elasticsearch.domain.SupplierDoc;

import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Elasticsearch service implementation class for Supplier document.
 */
@Service
public class SupplierSearchServiceImpl extends AbstractElasticsearchService
    implements SupplierSearchService {

  @Override
  public String keyAlias() {
    return "suppliers";
  }

  @Override
  public List<SupplierDoc> getTop10Suppliers() {
    final NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
        .withIndices(index())
        .withPageable(PageUtils.DEF_PAGE)
        .withQuery(QueryBuilders.matchAllQuery());
    return searchList(queryBuilder.build(), SupplierDoc.class);
  }

  @Override
  public List<SupplierDoc> getAll() {
    return searchStream(allSearchQueryBuilder(index()).build(), SupplierDoc.class);
  }
}
