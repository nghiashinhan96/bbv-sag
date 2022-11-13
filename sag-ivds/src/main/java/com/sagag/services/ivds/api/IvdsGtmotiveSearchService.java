package com.sagag.services.ivds.api;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.ivds.request.gtmotive.GtmotiveOperationRequest;
import com.sagag.services.ivds.response.GtmotiveResponse;

public interface IvdsGtmotiveSearchService {

  /**
   * Returns the GTMotive selected parts from Gtmotive Reference Codes.
   *
   * @param user the current user who requests.
   * @param request the operation request.
   * @return a response of {@link GtmotiveResponse}
   */
  GtmotiveResponse searchArticlesByGtOperations(UserInfo user, GtmotiveOperationRequest request);
}
