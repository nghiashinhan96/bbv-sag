package com.sagag.services.ivds.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.domain.article.ArticleDocDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThuleArticleListSearchResponse {

  private List<ArticleDocDto> thuleArticles;

  private List<String> notFoundPartNumbers;

  @JsonIgnore
  public boolean isPresent() {
    return !CollectionUtils.isEmpty(notFoundPartNumbers)
        || !CollectionUtils.isEmpty(thuleArticles);
  }

  public static ThuleArticleListSearchResponse empty() {
    return ThuleArticleListSearchResponse.builder().thuleArticles(Collections.emptyList())
        .notFoundPartNumbers(Collections.emptyList()).build();
  }

}
