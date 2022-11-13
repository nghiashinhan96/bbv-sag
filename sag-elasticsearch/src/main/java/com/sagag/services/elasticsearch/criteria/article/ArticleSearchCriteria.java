package com.sagag.services.elasticsearch.criteria.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleSearchCriteria {

  private List<String> suppliers;

  private List<String> gaIds;

  private String artNum;

  private String idSagsys;

  private List<String> idSagsyses;

  private String artId;

  private String pnrn;

  private String freetext;

  private List<String> supplierRaws;
}
