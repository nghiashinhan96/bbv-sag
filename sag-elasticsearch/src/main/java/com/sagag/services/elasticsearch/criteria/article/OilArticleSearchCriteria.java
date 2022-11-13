package com.sagag.services.elasticsearch.criteria.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OilArticleSearchCriteria extends ArticleAggregateCriteria {

  private List<String> vehicles; // Fahrzeug-Art

  private List<String> aggregates; // Aggregat

  private List<String> viscosities; // Viskosität

  private List<String> bottleSizes; // Gebindegrösse

  private List<String> approved; // Freigabe

  private List<String> specifications; // Spezifikation

  private List<String> brands;
}
