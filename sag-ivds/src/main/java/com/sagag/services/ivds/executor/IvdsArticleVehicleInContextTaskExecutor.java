package com.sagag.services.ivds.executor;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;

import java.util.List;
import java.util.Optional;

public interface IvdsArticleVehicleInContextTaskExecutor {

  /**
   * Executes update articles information with vehicle in context.
   *
   * @param user
   * @param articles
   * @param vehInContextOpt
   * @param additionalCriteria
   * @param args the additional arguments
   * @return the updated articles
   */
  List<ArticleDocDto> execute(UserInfo user, List<ArticleDocDto> articles,
      Optional<VehicleDto> vehInContextOpt, Optional<AdditionalSearchCriteria> additionalCriteria,
      Object... args);
}
