package com.sagag.services.ivds.request.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.elasticsearch.criteria.article.OilArticleSearchCriteria;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(value = Include.NON_NULL)
public class OilArticleSearchRequest
  implements Serializable, ArticleSearchCriteriaConverter<OilArticleSearchCriteria> {

  private static final long serialVersionUID = 5941542499005873222L;

  private List<String> vehicles; // Fahrzeug-Art

  private List<String> aggregates; // Aggregat

  private List<String> viscosities; // Viskosität

  @JsonProperty("bottle_sizes")
  private List<String> bottleSizes; // Gebindegrösse

  @JsonProperty("approved_list")
  private List<String> approved; // Freigabe

  private List<String> specifications; // Spezifikation

  private List<String> brands;

  @Override
  public OilArticleSearchCriteria toCriteria() {
    return OilArticleSearchCriteria.builder()
        .vehicles(getVehicles())
        .aggregates(getAggregates())
        .viscosities(getViscosities())
        .bottleSizes(getBottleSizes())
        .approved(getApproved())
        .specifications(getSpecifications())
        .brands(getBrands())
        .build();
  }
}
