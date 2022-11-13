package com.sagag.services.stakis.erp.converter.impl.article;

import org.springframework.stereotype.Component;

import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.domain.article.ArticleDocDto;

@Component
@CzProfile
public class TmGetPriceAndStockResponseConverterImpl
  extends AbstractTmGetErpInformationResponseConverterImpl {

  @Override
  protected void fillErpResponseByContextId(ArticleDocDto originalArt, ArticleDocDto erpResultArt) {
    if (originalArt == null || erpResultArt == null) {
      return;
    }
    originalArt.setArticle(erpResultArt.getArticle());
    originalArt.setDepositArticle(erpResultArt.getDepositArticle());
    originalArt.setMemos(erpResultArt.getMemos());
    originalArt.setPrice(erpResultArt.getPrice());
    originalArt.setAllowedAddToShoppingCart(erpResultArt.isAllowedAddToShoppingCart());
  }

}
