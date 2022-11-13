package com.sagag.services.stakis.erp.converter.impl.article;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.xml.bind.JAXBElement;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.stakis.erp.utils.ErpInformationExtractors;
import com.sagag.services.stakis.erp.wsdl.tmconnect.AvailabilityState;
import com.sagag.services.stakis.erp.wsdl.tmconnect.EntityLink;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ErpInformation;
import com.sagag.services.stakis.erp.wsdl.tmconnect.LinkedItemsCollection;

@Component
@CzProfile
public class TmDepositItemConverter {

  private static final int DEPOSIT_ROLE_TYPE = 6;

  @Autowired
  private TmArticleConverter articleConverter;

  public Optional<ArticleDocDto> apply(List<LinkedItemsCollection> linkedItems,
      Map<String, String> articleIdUuidMap,
      Map<String, AvailabilityState> availabilityStateTypes, String language,
      double vatRate, Integer quantity) {
    if (CollectionUtils.isEmpty(linkedItems) || MapUtils.isEmpty(articleIdUuidMap)) {
      return Optional.empty();
    }

    final Optional<LinkedItemsCollection> linkedItemsCollectionOpt = linkedItems.stream()
        .filter(i -> i.getRole() == DEPOSIT_ROLE_TYPE).findFirst();
    if (!linkedItemsCollectionOpt.isPresent()) {
      return Optional.empty();
    }
    final LinkedItemsCollection linkedItemsCol = linkedItemsCollectionOpt.get();
    final List<ErpInformation> erpInfos =
        ErpInformationExtractors.extractErpInformationList(linkedItemsCol);

    final String artUUID = findUUIDFromErpInformation(erpInfos);
    final String artId = findArtIdByUUID(articleIdUuidMap, artUUID);
    if (StringUtils.isBlank(artId)) {
      return Optional.empty();
    }

    final ArticleDocDto article = new ArticleDocDto();
    article.setId(artId);
    article.setIdSagsys(artId);
    article.setArtid(artId);
    article.setAmountNumber(quantity);

    return Optional.of(articleConverter.convert(article, articleIdUuidMap, erpInfos,
        availabilityStateTypes, language, vatRate));
  }

  private static String findUUIDFromErpInformation(final List<ErpInformation> erpInfos) {
    return erpInfos.stream().findFirst().map(ErpInformation::getItem)
        .map(JAXBElement::getValue).map(EntityLink::getGuid)
        .orElse(StringUtils.EMPTY);
  }

  private static String findArtIdByUUID(Map<String, String> articleIdUuidMap, String uuid) {
    for (Entry<String, String> entry : articleIdUuidMap.entrySet()) {
      if (StringUtils.equals(entry.getValue(), uuid)) {
        return entry.getKey();
      }
    }
    return StringUtils.EMPTY;
  }

}
