package com.sagag.services.stakis.erp.converter.impl.article;

import org.springframework.stereotype.Component;

import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.domain.article.ArticleDocDto;

@Component
@CzProfile
public class TmGetAvailabilitiesResponseConverterImpl
  extends AbstractTmGetErpInformationResponseConverterImpl {

  @Override
  protected void fillErpResponseByContextId(ArticleDocDto originalArt, ArticleDocDto erpResultArt) {
    if (originalArt == null || erpResultArt == null) {
      return;
    }
    originalArt.setAvailabilities(erpResultArt.getAvailabilities());
  }

  @Override
  protected boolean isErpInfoContextId() {
    return true;
  }
}
