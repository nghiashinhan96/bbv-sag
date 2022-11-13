package com.sagag.services.ivds.api;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.ivds.request.CategorySearchRequest;
import com.sagag.services.ivds.response.ArticleListSearchResponseDto;

public interface IvdsArticleInVehicleContextService {

  /**
   * Returns all articles in the Fitment that available for specific categories and vehicles.
   *
   * @param user the user login
   * @param categorySearchRequest search request
   * @return the search response object
   * @throw
   * {@link ServiceException}
   */
  ArticleListSearchResponseDto searchArticlesInVehicleContext(UserInfo user,
      CategorySearchRequest categorySearchRequest) throws ServiceException;


}
