package com.sagag.services.elasticsearch.api;

import com.sagag.services.elasticsearch.domain.article.ArticleVehicles;
import java.util.List;


public interface ArticleVehiclesSearchService {

  /**
   * search the list of vehicle usages by article id
   *
   * @param articleId the of article id
   * @return list of {@link ArticleVehicles}
   */
  List<ArticleVehicles> searchArticleVehiclesByArtId(String articleId);

}
