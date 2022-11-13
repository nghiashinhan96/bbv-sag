package com.sagag.services.ivds.executor;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.ivds.request.GetArticleSyncInformation;
import com.sagag.services.ivds.response.GetArticleInformationResponse;

public interface IvdsArticleErpInformationTaskExecutor {

  /**
   * Returns the Erp infomation(availability, price and stock) of selected articles.
   *
   */
  GetArticleInformationResponse executeErpInformation(UserInfo user,
      GetArticleSyncInformation request);

}
