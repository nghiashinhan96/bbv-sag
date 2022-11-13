package com.sagag.services.elasticsearch.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.common.contants.UniversalPartConstants;
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
public class UniversalPartArticleResponse implements IArticleResponseSupport {

  private Page<ArticleDoc> articles;
  private Map<String, List<Object>> aggregations;

  @JsonIgnore
  @Override
  public boolean hasArticles() {
    return hasArticles(articles);
  }

  @JsonIgnore
  @Override
  public long totalArticles() {
    if (!hasArticles()) {
      return NumberUtils.LONG_ZERO;
    }
    return articles.getTotalElements();
  }

  @JsonIgnore
  @Override
  public boolean allowViewArticles() {
    return totalArticles() <= UniversalPartConstants.VIEW_WSP_ARTICLES_MAX_PAGE_SIZE;
  }

  @JsonIgnore
  @Override
  public boolean hasAggregations() {
    return MapUtils.isNotEmpty(aggregations);
  }

  @JsonIgnore
  @Override
  public Map<String, List<Object>> aggregations() {
    return getAggregations();
  }

  public static UniversalPartArticleResponse empty() {
    return UniversalPartArticleResponse.builder().articles(Page.empty()).build();
  }

}
