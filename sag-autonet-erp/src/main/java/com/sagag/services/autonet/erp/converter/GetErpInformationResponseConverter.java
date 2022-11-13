package com.sagag.services.autonet.erp.converter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBElement;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.converter.IGetErpInformationResponseConverter;
import com.sagag.services.autonet.erp.wsdl.tmconnect.ArrayOfArticleTmfType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.ArrayOfAvailabilityStateType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.ArrayOfErpInformationType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.ArticleTmfType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.AvailabilityStateType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.ErpInformationType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.GetErpInformationReplyType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.GetErpInformationResponseBodyType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.MasterDataType;
import com.sagag.services.common.profiles.AutonetProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.autonet.erp.AutonetArticleInfos;

@Component
@AutonetProfile
public class GetErpInformationResponseConverter
  implements IGetErpInformationResponseConverter<GetErpInformationResponseBodyType> {

  @Autowired
  private GetErpInformationPricesFinder pricesFinder;

  @Autowired
  private GetErpInformationAvailabilitiesFinder availabilityFinder;

  @Autowired
  private GetErpInformationMemosFinder memoFinder;

  @Override
  public List<ArticleDocDto> apply(List<ArticleDocDto> articles,
      GetErpInformationResponseBodyType info, double vatRate, String lang) {
    if (CollectionUtils.isEmpty(articles) || Objects.isNull(info)) {
      return articles;
    }

    List<ErpInformationType> erpInfos = getErpInformationTypes(info);
    if (CollectionUtils.isEmpty(erpInfos)) {
      return articles;
    }

    List<String> autonetIds =
        articles.stream().map(ArticleDocDto::getIdSagsys).collect(Collectors.toList());
    if (CollectionUtils.isEmpty(autonetIds)) {
      return articles;
    }

    MasterDataType masterDataType = getMasterDataType(info);
    if (Objects.isNull(masterDataType)) {
      return articles;
    }

    List<ArticleTmfType> articleTmfTypes = getArticleTmfs(masterDataType);
    if (CollectionUtils.isEmpty(articleTmfTypes)) {
      return articles;
    }

    List<ArticleTmfType> validArticleTmfTypes =
        articleTmfTypes.stream()
            .filter(item -> autonetIds.contains(Optional.ofNullable(item.getArticleIdErp())
                .map(JAXBElement::getValue).orElse(StringUtils.EMPTY)))
            .collect(Collectors.toList());
    if (CollectionUtils.isEmpty(validArticleTmfTypes)) {
      return articles;
    }

    final BinaryOperator<String> mergerFunction = (oldUuid, newUuid) -> newUuid;
    final Map<String, String> articleIdUuidMap =
        validArticleTmfTypes.stream().filter(item -> Objects.nonNull(item.getArticleIdErp()))
            .collect(Collectors.toMap(articleAutonetIdFunc(), ArticleTmfType::getGuid, mergerFunction));
    if (MapUtils.isEmpty(articleIdUuidMap)) {
      return articles;
    }

    List<AvailabilityStateType> availabilityStateTypes = getAvailabilityStateTypes(masterDataType);

    return doItemsFindAutonetArticleInfos(articles, articleIdUuidMap, availabilityStateTypes,
        erpInfos);
  }

  private Function<ArticleTmfType, String> articleAutonetIdFunc() {
    return article -> Optional.ofNullable(article.getArticleIdErp()).map(JAXBElement::getValue)
        .orElse(StringUtils.EMPTY);
  }

  private List<ArticleDocDto> doItemsFindAutonetArticleInfos(List<ArticleDocDto> articles,
      Map<String, String> articleIdMap, List<AvailabilityStateType> availabilityStateTypes,
      List<ErpInformationType> erpInfos) {
    return articles.stream().map(item -> doItemFindAutonetArticleInfos(item, articleIdMap,
        availabilityStateTypes, erpInfos)).collect(Collectors.toList());
  }

  private ArticleDocDto doItemFindAutonetArticleInfos(ArticleDocDto article,
      Map<String, String> articleIdMap, List<AvailabilityStateType> availabilityStateTypes,
      List<ErpInformationType> erpInfos) {
    String uuid = articleIdMap.get(article.getIdSagsys());
    if (StringUtils.isEmpty(uuid)) {
      return article;
    }

    AutonetArticleInfos autonetInfos = new AutonetArticleInfos();
    pricesFinder.process(uuid, erpInfos).ifPresent(autonetInfos::setPrices);
    availabilityFinder.process(uuid, erpInfos, availabilityStateTypes)
        .ifPresent(autonetInfos::setAvailability);
    memoFinder.process(uuid, erpInfos).ifPresent(autonetInfos::setMemos);

    article.setAutonetInfos(autonetInfos);
    return article;
  }

  private List<ErpInformationType> getErpInformationTypes(
      GetErpInformationResponseBodyType result) {
    JAXBElement<GetErpInformationReplyType> getErpInformationReplyType =
        result.getGetErpInformationResult();

    if (Objects.isNull(getErpInformationReplyType)) {
      return Collections.emptyList();
    }

    JAXBElement<ArrayOfErpInformationType> erpArticleInformations =
        Optional.ofNullable(getErpInformationReplyType.getValue())
            .map(GetErpInformationReplyType::getErpArticleInformation).orElse(null);
    if (Objects.isNull(erpArticleInformations)) {
      return Collections.emptyList();
    }
    return Optional.ofNullable(erpArticleInformations.getValue())
        .map(ArrayOfErpInformationType::getErpInformation).orElse(Collections.emptyList());
  }

  private List<ArticleTmfType> getArticleTmfs(MasterDataType masterDataType) {
    JAXBElement<ArrayOfArticleTmfType> arrayOfArticleTmfType = masterDataType.getArticleTmfs();
    if (Objects.isNull(arrayOfArticleTmfType)) {
      return Collections.emptyList();
    }
    return Optional.ofNullable(arrayOfArticleTmfType.getValue())
        .map(ArrayOfArticleTmfType::getArticleTmf).orElse(Collections.emptyList());
  }

  private List<AvailabilityStateType> getAvailabilityStateTypes(MasterDataType masterDataType) {
    JAXBElement<ArrayOfAvailabilityStateType> arrayOfAvailabilityStateType =
        masterDataType.getAvailabilityStates();
    if (Objects.isNull(arrayOfAvailabilityStateType)) {
      return Collections.emptyList();
    }
    return Optional.ofNullable(arrayOfAvailabilityStateType.getValue())
        .map(ArrayOfAvailabilityStateType::getAvailabilityState).orElse(Collections.emptyList());
  }

  private MasterDataType getMasterDataType(GetErpInformationResponseBodyType result) {
    JAXBElement<GetErpInformationReplyType> getErpInformationReplyType =
        result.getGetErpInformationResult();
    if (Objects.isNull(getErpInformationReplyType)) {
      return null;
    }
    JAXBElement<MasterDataType> masterDataType =
        Optional.ofNullable(getErpInformationReplyType.getValue())
            .map(GetErpInformationReplyType::getMasterData).orElse(null);
    return Optional.ofNullable(masterDataType).map(JAXBElement::getValue).orElse(null);
  }
}
