package com.sagag.services.tools.batch.offer_feature.convert_id_sagsys;

import java.util.Optional;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.elasticsearch.ArticleDoc;
import com.sagag.services.tools.domain.target.TargetOfferPosition;
import com.sagag.services.tools.service.ArticleSearchService;

import lombok.extern.slf4j.Slf4j;

@Component
@StepScope
@Slf4j
@OracleProfile
public class ConvertingIdSagsysProcessor implements ItemProcessor<TargetOfferPosition, TargetOfferPosition> {

  @Autowired
  private ArticleSearchService articleSearchService;

  @Override
  public TargetOfferPosition process(final TargetOfferPosition source) {
    final String idUmsart = source.getUmsartId();
    Optional<ArticleDoc> vehDocOpt = articleSearchService.searchArticleByIdUmsart(idUmsart);
    if (!vehDocOpt.isPresent()) {
      log.warn("Not found any id sagsys");
      return null;
    }
    final String idSagsys = vehDocOpt.get().getIdSagsys();
    log.debug("Found id_sagsys by id_umsart = {} --> id_sagsys = {}", idUmsart, idSagsys);
    source.setSagsysId(idSagsys);
    return source;
  }
}
