package com.sagag.services.elasticsearch.criteria.article;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BaseTyreArticleSearchCriteria extends ArticleAggregateCriteria {

  private String widthCvp;
  private String heightCvp;
  private String radiusCvp;
  private String supplier;
  private boolean runflat;
  private boolean spike;

  private List<String> speedIndexCvps;

  private List<String> tyreSegmentCvps;

  private List<String> loadIndexCvps;

}
