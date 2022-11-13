package com.sagag.services.ivds.api;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.ivds.response.ThuleArticleListSearchResponse;

import java.util.Map;

public interface IvdsThuleService {

  /**
   * Returns the found articles from form data of Thule.
   *
   * @param user the authed user
   * @param formData the Thule form data
   * @return the search response.
   */
  ThuleArticleListSearchResponse searchArticlesByBuyersGuide(UserInfo user,
      Map<String, String> formData);
}
