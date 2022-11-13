package com.sagag.services.elasticsearch.dto;

import com.google.common.collect.Maps;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.extractor.SagBucket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.collections4.MapUtils;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleFilteringResponse {

  private Page<ArticleDoc> articles;
  private Map<String, List<SagBucket>> aggregations;

  public static ArticleFilteringResponse empty() {
    return of(Page.empty());
  }

  public static ArticleFilteringResponse of(Page<ArticleDoc> articles) {
    return ArticleFilteringResponse.builder().articles(articles)
        .aggregations(Maps.newHashMap()).build();
  }

  /**
   * Checks if the response has content or not.
   *
   * @return <code> true </code> if the response has articles, <code>false</code> otherwise.
   */
  public boolean hasContent() {
    return this.articles != null && this.articles.hasContent();
  }

  public boolean hasAggregations() {
    return !MapUtils.isEmpty(this.getAggregations());
  }
}
