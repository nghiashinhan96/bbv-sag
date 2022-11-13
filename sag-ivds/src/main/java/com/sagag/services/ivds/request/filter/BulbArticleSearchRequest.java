package com.sagag.services.ivds.request.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sagag.services.elasticsearch.criteria.article.BulbArticleSearchCriteria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class BulbArticleSearchRequest
    implements Serializable, ArticleSearchCriteriaConverter<BulbArticleSearchCriteria> {

  private static final long serialVersionUID = 4917621352105635438L;

  private List<String> suppliers; // Hersteller

  private List<String> voltages;

  private List<String> watts;

  private List<String> codes; // code & bild

  @Override
  public BulbArticleSearchCriteria toCriteria() {
    return BulbArticleSearchCriteria.builder()
        .suppliers(getSuppliers())
        .voltages(getVoltages())
        .watts(getWatts())
        .codes(getCodes())
        .build();
  }
}
