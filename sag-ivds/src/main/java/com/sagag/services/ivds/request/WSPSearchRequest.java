package com.sagag.services.ivds.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.elasticsearch.criteria.article.wsp.UniversalPartArticleSearchCriteria;
import com.sagag.services.elasticsearch.criteria.article.wsp.UniversalPartArticleSearchTerm;
import com.sagag.services.ivds.converter.wsp.UniversalPartConverters;
import com.sagag.services.ivds.domain.WSPCriteriaDto;
import com.sagag.services.ivds.request.filter.ArticleSearchCriteriaConverter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Optional;

/**
 * Request body class for WSP search.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WSPSearchRequest
    implements Serializable, ArticleSearchCriteriaConverter<UniversalPartArticleSearchCriteria> {

  private static final long serialVersionUID = -3754705117549123043L;

  private int size;

  @JsonProperty("offset")
  private int offset;

  private WSPCriteriaDto includeCriteria;

  private WSPCriteriaDto excludeCriteria;

  private String language;

  @Override
  public UniversalPartArticleSearchCriteria toCriteria() {
    final UniversalPartArticleSearchTerm includeSearchTerm = Optional.ofNullable(includeCriteria)
        .map(UniversalPartConverters.simpleWspCriteriaArticleSearchConverter()).orElse(null);

    final UniversalPartArticleSearchTerm excludeSearchTerm = Optional.ofNullable(excludeCriteria)
        .map(UniversalPartConverters.simpleWspCriteriaArticleSearchConverter()).orElse(null);

    return new UniversalPartArticleSearchCriteria(includeSearchTerm, excludeSearchTerm, language);
  }

}
