package com.sagag.services.ivds.api;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.ivds.request.WSPSearchRequest;
import com.sagag.services.ivds.response.wsp.UniversalPartArticleSearchResponse;

import org.springframework.data.domain.Pageable;

public interface IvdsArticleInWSPContextService {

  /**
   * Returns all genarts with brands of articles in the criterion of universal part leaf setting.
   *
   * @param user the user login
   * @param request search request
   * @return the search response object {@link UniversalPartArticleSearchResponse}
   */
  UniversalPartArticleSearchResponse searchUniversalPartArticlesByRequest(UserInfo user,
      WSPSearchRequest request, Pageable pageable);

}
