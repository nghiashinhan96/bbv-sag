package com.sagag.services.tools.service;

import java.util.Optional;

import com.sagag.services.tools.domain.elasticsearch.ArticleDoc;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface ArticleSearchService {

  @Getter
  @AllArgsConstructor
  enum ArticleField {
    ID_UMSART("id_umsart");
    private String field;
  }

  Optional<ArticleDoc> searchArticleByIdUmsart(final String idUmsart);

}
