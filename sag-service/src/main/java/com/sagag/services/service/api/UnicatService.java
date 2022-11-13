package com.sagag.services.service.api;

import com.sagag.eshop.service.dto.UserInfo;

public interface UnicatService {

  /**
   * @param user
   * @return
   */
  String getUnicatCatalogUri(UserInfo user);
}
