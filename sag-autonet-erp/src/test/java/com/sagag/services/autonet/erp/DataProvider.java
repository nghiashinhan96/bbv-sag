package com.sagag.services.autonet.erp;

import com.sagag.services.autonet.erp.domain.AutonetErpUserInfo;
import com.sagag.services.domain.article.ArticleDocDto;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DataProvider {

  public AutonetErpUserInfo buildAutonetUserInfo() {
    AutonetErpUserInfo userInfo = new AutonetErpUserInfo();
    userInfo.setUsername("realsergiu_test");
    userInfo.setCustomerId("17665");
    userInfo.setSecurityToken("94C88E9D-330E-4157-A823-A9CE94ED739F");
    return userInfo;
  }

  public ArticleDocDto buildArticleInfo() {
    final ArticleDocDto article = new ArticleDocDto();
    article.setIdSagsys("LP1706");
    article.setAmountNumber(100);
    article.setIdDlnr("3");
    article.setArtnrDisplay("13.0460-7143.2");
    return article;
  }

}
