package com.sagag.services.ivds.filter.additional_recommendation;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.article.ArticleInfoDto;
import com.sagag.services.oates.dto.OatesApplicationDto;

import java.util.List;
import java.util.Optional;

public interface AdditionalRecommendationFilter {

  /**
   * Filters for note of additional recommendation articles.
   *
   * @param user user info
   * @return the filtered additional recommendation with note
   */
  Optional<ArticleInfoDto> filterAdditionalRecommendation(UserInfo user);

  /**
   * Filter additional recommendation articles
   * @param user
   * @param filteredApplications
   * @return additional recommendation article ids
   */
  List<String> filterAdditionalRecommendationArticles(UserInfo user,
      List<OatesApplicationDto> filteredApplications);
}
