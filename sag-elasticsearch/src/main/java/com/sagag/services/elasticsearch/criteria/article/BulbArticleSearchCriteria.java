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
public class BulbArticleSearchCriteria extends ArticleAggregateCriteria {

  private List<String> suppliers; // Hersteller

  private List<String> voltages;

  private List<String> watts;

  private List<String> codes; // code & bidld

}
