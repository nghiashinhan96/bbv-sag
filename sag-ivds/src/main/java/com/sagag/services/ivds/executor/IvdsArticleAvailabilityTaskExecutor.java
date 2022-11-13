package com.sagag.services.ivds.executor;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.ArticleStock;
import com.sagag.services.ivds.request.availability.GetArticleInformation;
import com.sagag.services.ivds.response.availability.GetArticleAvailabilityResponse;

import java.util.List;

public interface IvdsArticleAvailabilityTaskExecutor {

  /**
   * Returns the availability of selected article id.
   * @param totalAxStock;
   *
   */
  List<ArticleDocDto> executeAvailability(UserInfo user, String idSagSys,
      Integer amountNumber, ArticleStock stock, Double totalAxStock);

  /**
   * Returns the availability of selected articles by request.
   *
   */
  GetArticleAvailabilityResponse executeAvailabilities(final UserInfo user,
      final GetArticleInformation request);

}
