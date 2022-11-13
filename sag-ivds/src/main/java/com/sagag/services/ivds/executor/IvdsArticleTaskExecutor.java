package com.sagag.services.ivds.executor;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.ivds.domain.ArticleExternalRequestOption;

import java.util.List;
import java.util.Optional;

public interface IvdsArticleTaskExecutor {

  /**
   * Executes the some tasks for IVDS components.
   *
   * @param user the user login
   * @param articles the list of articles need update
   * @param vehicleOpt the selected vehicle
   * @param option the requests are requested to update.
   * @return the updated list of {@link ArticleDocDto}
   */
  List<ArticleDocDto> executeTask(UserInfo user, List<ArticleDocDto> articles,
      Optional<VehicleDto> vehicleOpt, ArticleExternalRequestOption option);

}
