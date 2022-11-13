package com.sagag.services.elasticsearch.converter;

import com.sagag.services.common.enums.RelevanceGroupType;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.domain.article.ExternalPartDoc;
import java.util.function.Function;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

@Component
public class ExternalPartConverter implements Function<ExternalPartDoc, ArticleDoc> {

  private static final String FROM_EXTERNAL_PART_GA_ID = NumberUtils.INTEGER_ZERO.toString();

  @Override
  public ArticleDoc apply(ExternalPartDoc externalPartDoc) {
    ArticleDoc articleDoc = SagBeanUtils.map(externalPartDoc, ArticleDoc.class);
    final String prnr = externalPartDoc.getPrnr();
    articleDoc.setArtnr(prnr);

    final String artId = externalPartDoc.getIdPim();
    articleDoc.setId(artId);
    articleDoc.setArtid(artId);
    articleDoc.setIdSagsys(artId);
    articleDoc.setIdAutonet(artId);
    articleDoc.setArtnrDisplay(externalPartDoc.getPrnrd());
    articleDoc.setIdDlnr(externalPartDoc.getBrandId());
    articleDoc.setProductAddon(externalPartDoc.getProductAddon());
    articleDoc.setIsReplacementFor(externalPartDoc.getIsReplacement());
    articleDoc.setHasReplacement(externalPartDoc.getHasReplacement());
    articleDoc.setGaId(FROM_EXTERNAL_PART_GA_ID);
    articleDoc.setRelevanceGroupType(RelevanceGroupType.ORIGINAL_PART);
    return articleDoc;
  }
}
