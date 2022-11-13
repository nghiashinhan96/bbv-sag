package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.elasticsearch.api.AbstractElasticsearchService;
import com.sagag.services.elasticsearch.api.BrandPriorityService;
import com.sagag.services.elasticsearch.domain.brand_sorting.BrandPriorityDoc;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandPriorityServiceImpl extends AbstractElasticsearchService
  implements BrandPriorityService {

  @Override
  public String keyAlias() {
    return "brand_priority";
  }

  @Override
  public List<BrandPriorityDoc> getAll() {
    return searchStream(matchAllSearchQueryByIndex().apply(index()), BrandPriorityDoc.class);
  }

}
