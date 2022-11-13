package com.sagag.services.elasticsearch.config;

import com.sagag.services.elasticsearch.domain.article.FitmentArticle;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.enums.Index;
import java.util.function.Function;

public interface ArticleIdFieldMapper {

  ArticleField getField();

  Index.Fitment getArtIdFitment();

  Function<FitmentArticle, String> articleIdExtractor();
}
