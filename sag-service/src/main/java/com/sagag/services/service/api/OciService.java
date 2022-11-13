package com.sagag.services.service.api;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;

public interface OciService {

  /**
   * Exports OCI data form.
   *
   * @param user the logged in user info
   * @param hookUrl the returned hookUrl
   * @return html content of Oci form
   */
  String exportOrder(UserInfo user, String hookUrl, ShopType shopType);

}
