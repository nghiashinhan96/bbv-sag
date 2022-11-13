package com.sagag.services.elasticsearch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.elasticsearch.common.OilConstants;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OilArticleResponse implements IArticleResponseSupport {

  private Page<ArticleDoc> articles;
  private Map<String, List<Object>> aggregations;

  @JsonProperty("has_articles")
  @Override
  public boolean hasArticles() {
    return hasArticles(articles);
  }

  @Override
  public long totalArticles() {
    if (!hasArticles()) {
      return NumberUtils.LONG_ZERO;
    }
    return articles.getTotalElements();
  }

  @Override
  public boolean allowViewArticles() {
    return hasArticles() && totalArticles() <= OilConstants.VIEW_ARTICLES_MAX_PAGE_SIZE;
  }

  @JsonProperty("has_aggregations")
  @Override
  public boolean hasAggregations() {
    return MapUtils.isNotEmpty(aggregations);
  }

  @Override
  public Map<String, List<Object>> aggregations() {
    return getAggregations();
  }

}
