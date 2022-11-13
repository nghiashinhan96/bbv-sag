package com.sagag.services.article.api.domain.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdditionalSearchCriteria implements Serializable {

  private static final long serialVersionUID = -6734853407290397258L;

  private String searchString;

  private boolean detailArticleRequest;

  private String kTypeNr;

  private Boolean isExcludeSubArticles;

}
