package com.sagag.services.elasticsearch.api.impl.articles;

import com.sagag.services.elasticsearch.api.AbstractElasticsearchService;

public class AbstractArticleElasticsearchService extends AbstractElasticsearchService {

  @Override
  public String keyAlias() {
    return "articles";
  }
}
