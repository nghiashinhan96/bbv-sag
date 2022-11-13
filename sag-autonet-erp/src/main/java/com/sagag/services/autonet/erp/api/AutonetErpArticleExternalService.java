package com.sagag.services.autonet.erp.api;

import com.sagag.services.article.api.SoapArticleErpExternalService;
import com.sagag.services.autonet.erp.domain.AutonetErpUserInfo;

public interface AutonetErpArticleExternalService extends SoapArticleErpExternalService {

  default AutonetErpUserInfo buildUserInfo(String username, String customerId, String securityToken,
      String lang) {
    AutonetErpUserInfo userInfo = new AutonetErpUserInfo();
    userInfo.setUsername(username);
    userInfo.setCustomerId(customerId);
    userInfo.setSecurityToken(securityToken);
    userInfo.setLang(lang);
    return userInfo;
  }
}
