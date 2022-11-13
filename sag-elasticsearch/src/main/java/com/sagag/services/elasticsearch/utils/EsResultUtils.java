package com.sagag.services.elasticsearch.utils;

import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.dto.TyreArticleResponse;
import com.sagag.services.elasticsearch.exception.ResultOverLimitException;

import lombok.experimental.UtilityClass;

import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.function.Supplier;

@UtilityClass
public class EsResultUtils {

  public static ArticleFilteringResponse getOrThrowIfResultOverLimit(
      final Supplier<ArticleFilteringResponse> supplier, final int limitNr) {
    return Optional.ofNullable(supplier.get())
        .filter(res -> !greaterMoreThan(res.getArticles(), limitNr))
        .orElseThrow(ResultOverLimitException::new);
  }

  public static TyreArticleResponse getOrThrowIfTyreResultOverLimit(
      final Supplier<TyreArticleResponse> supplier, final int limitNr) {
    return Optional.ofNullable(supplier.get())
        .filter(res -> !greaterMoreThan(res.getArticles(), limitNr))
        .orElseThrow(ResultOverLimitException::new);
  }

  private static boolean greaterMoreThan(final Page<ArticleDoc> articles, final int value) {
    return articles.hasContent() && articles.getNumberOfElements() > value;
  }
}
