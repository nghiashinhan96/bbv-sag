package com.sagag.services.ivds.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.elasticsearch.extractor.SagBucket;

import lombok.Data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Data
public class FilteredArticleAndAggregationResponse {

  private Page<ArticleDocDto> articles;

  private Map<String, List<SagBucket>> aggregations;

  public static FilteredArticleAndAggregationResponse empty(Pageable pageable) {
    final FilteredArticleAndAggregationResponse emptyRes = new FilteredArticleAndAggregationResponse();
    emptyRes.setAggregations(Maps.newHashMap());
    emptyRes.setArticles(Page.empty(pageable));
    return emptyRes;
  }

  public boolean hasArticles() {
    return this.articles.hasContent();
  }

  @JsonIgnore
  public boolean hasContent() {
    return articles != null && articles.hasContent();
  }

}
