package com.sagag.services.stakis.erp.converter.impl.article;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.sagag.services.article.api.converter.IGetErpInformationResponseConverter;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.stakis.erp.utils.ErpInformationExtractors;
import com.sagag.services.stakis.erp.wsdl.tmconnect.AvailabilityState;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ErpInformation;
import com.sagag.services.stakis.erp.wsdl.tmconnect.GetErpInformationResponseBody;
import com.sagag.services.stakis.erp.wsdl.tmconnect.MasterData;

public abstract class AbstractTmGetErpInformationResponseConverterImpl
  implements IGetErpInformationResponseConverter<GetErpInformationResponseBody> {

  @Autowired
  private TmArticleConverter articleConverter;

  @Override
  public List<ArticleDocDto> apply(List<ArticleDocDto> articles,
      GetErpInformationResponseBody result, double vatRate, String language) {
    if (CollectionUtils.isEmpty(articles)) {
      return ListUtils.emptyIfNull(articles);
    }

    final List<ErpInformation> erpInfos =
        ErpInformationExtractors.extractErpInformationList(result);
    if (CollectionUtils.isEmpty(erpInfos)) {
      return articles;
    }

    final Optional<MasterData> masterDataOpt = ErpInformationExtractors.extractMasterData(result);
    if (!masterDataOpt.isPresent()) {
      return articles;
    }

    final MasterData masterData = masterDataOpt.get();
    final Map<String, String> articleIdUuidMap =
        ErpInformationExtractors.extractArticleIdUUIDMap(masterData);
    if (MapUtils.isEmpty(articleIdUuidMap)) {
      return articles;
    }

    final Map<String, AvailabilityState> availabilityStateTypesMasterData =
        ErpInformationExtractors.extractAvailabilityStateMap(masterData);
    articles.stream().forEach(art -> {
      final ArticleDocDto erpResultArt = articleConverter.convert(
          SagBeanUtils.map(art, ArticleDocDto.class), articleIdUuidMap, erpInfos,
          availabilityStateTypesMasterData, language, vatRate);
      fillErpResponseByContextId(art, erpResultArt);
      art.setQtyMultiple(handleQtyMultiple(erpResultArt.getQtyMultiple()));
      art.setSalesQuantity(NumberUtils.max(art.getQtyMultiple(), art.getSalesQuantity()));
    });
    return articles;
  }

  /**
   * Fills ERP data result to original article.
   *
   * @param originalArt
   * @param erpResultArt
   */
  protected abstract void fillErpResponseByContextId(ArticleDocDto originalArt,
      ArticleDocDto erpResultArt);

  protected boolean isErpInfoContextId() {
    return false;
  }

  private Integer handleQtyMultiple(Integer qtyMultiple) {
	if (Objects.isNull(qtyMultiple) || qtyMultiple <= 0) {
		return SagConstants.DEFAULT_MULTIPLE_QUANTITY;
	}
	return qtyMultiple;
  }

}
