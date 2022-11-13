package com.sagag.services.autonet.erp.builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.sagag.services.article.api.builder.IGetErpInformationRequestBuilder;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.autonet.erp.domain.AutonetErpUserInfo;
import com.sagag.services.autonet.erp.enums.MemoArticleSourceIdEnum;
import com.sagag.services.autonet.erp.wsdl.tmconnect.ArrayOfArticleTmfType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.ArrayOfErpInformationType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.ArrayOfMemoType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.ArticleTmfType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.CredentialsType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.EMasterDataTypeType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.EntityLinkType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.ErpInformationType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.GetErpInformationRequestBodyType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.GetErpInformationRequestType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.MasterDataType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.MemoType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.SupplierTmfType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.UserType;
import com.sagag.services.common.profiles.AutonetProfile;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.article.ArticleDocDto;

import lombok.extern.slf4j.Slf4j;

@Component
@AutonetProfile
@Slf4j
public class GetErpInformationRequestBuilder
    implements IGetErpInformationRequestBuilder<AutonetErpUserInfo, List<ArticleDocDto>,
      GetErpInformationRequestBodyType> {

  private static final int TYPE_OF_TMC_LABEL_MEMO = 5; // Autonet confirmed that tmc = 5 is fixed.

  @Autowired
  @Qualifier("autonetErpTmConnectObjectFactory")
  protected com.sagag.services.autonet.erp.wsdl.tmconnect.ObjectFactory tmcObjectFactory;

  @Override
  public GetErpInformationRequestBodyType buildRequest(AutonetErpUserInfo userInfo,
      List<ArticleDocDto> articles, Object... additionals) {
    log.debug("Building GetErpInformationRequest by user = {}", userInfo);
    Assert.notEmpty(articles, "The given articles must not be empty");

    final GetErpInformationRequestType request =
        tmcObjectFactory.createGetErpInformationRequestType();
    request.setLanguageCodeIso6391(
        tmcObjectFactory.createBaseRequestTypeLanguageCodeIso6391(userInfo.getLang()));
    request.setTimeStamp(DateUtils.newXMLGregorianCalendar(Instant.now()));
    request.setCredentials(tmcObjectFactory.createCredentials(buildCredentials(userInfo)));

    final Map<String, ArticleDocDto> articleMap = buildUUIDMap(articles);
    final MasterDataType masterData = buildMasterData(articleMap);
    request.setMasterData(tmcObjectFactory.createMasterData(masterData));
    request.setErpArticleInformation(
        tmcObjectFactory.createGetErpInformationRequestTypeErpArticleInformation(
            buildArrayOfErpInformation(articleMap, additionals)));
    // need to confirm about TypeId
    request.setTypeId(0);

    final GetErpInformationRequestBodyType erpInfo =
        tmcObjectFactory.createGetErpInformationRequestBodyType();
    erpInfo.setRequest(tmcObjectFactory.createGetErpInformationRequestBodyTypeRequest(request));
    return erpInfo;
  }

  protected CredentialsType buildCredentials(AutonetErpUserInfo userInfo) {
    Assert.notNull(userInfo, "The given Autonet ERP user info must not be null");
    final UserType userCredentials = tmcObjectFactory.createUserType();
    userCredentials.setUsername(tmcObjectFactory.createUserTypeUsername(userInfo.getUsername()));
    userCredentials
        .setCustomerId(tmcObjectFactory.createUserTypeCustomerId(userInfo.getCustomerId()));

    final CredentialsType credentials = tmcObjectFactory.createCredentialsType();
    credentials.setIsEncrypted(false);
    credentials.setCatalogUserCredentials(
        tmcObjectFactory.createCredentialsTypeCatalogUserCredentials((userCredentials)));
    credentials.setSecurityToken(
        tmcObjectFactory.createCredentialsTypeSecurityToken(userInfo.getSecurityToken()));
    return credentials;
  }

  protected MasterDataType buildMasterData(Map<String, ArticleDocDto> articles) {
    ArrayOfArticleTmfType arrayOfArticleTmf = tmcObjectFactory.createArrayOfArticleTmfType();

    articles.forEach((uuid, article) -> {
      final ArticleTmfType artTmf = tmcObjectFactory.createArticleTmfType();
      artTmf.setGuid(uuid);

      artTmf.setArticleIdErp(
          tmcObjectFactory.createArticleTypeArticleIdErp((article.getIdSagsys())));

      artTmf.setType(TYPE_ID_NORMAL);
      artTmf.setArticleIdSupplier(
          tmcObjectFactory.createArticleTmfTypeArticleIdSupplier(article.getArtnrDisplay()));

      final SupplierTmfType supplierTmf = tmcObjectFactory.createSupplierTmfType();
      if (StringUtils.isNotBlank(article.getIdDlnr())) {
        supplierTmf.setSupplierId(Integer.valueOf(article.getIdDlnr()));
      }

      supplierTmf.setPoolId(POOL_ID_TECDOC);
      artTmf.setSupplier(tmcObjectFactory.createArticleTmfTypeSupplier(supplierTmf));
      arrayOfArticleTmf.getArticleTmf().add(artTmf);
    });

    MasterDataType masterData = tmcObjectFactory.createMasterDataType();
    masterData.setArticleTmfs(tmcObjectFactory.createMasterDataTypeArticleTmfs(arrayOfArticleTmf));
    return masterData;
  }

  private ArrayOfErpInformationType buildArrayOfErpInformation(
      Map<String, ArticleDocDto> articles, Object[] additionals) {
    ArrayOfErpInformationType arrayOfErpInfo = tmcObjectFactory.createArrayOfErpInformationType();

    final Optional<AdditionalSearchCriteria> additional = Optional.ofNullable(additionals)
        .filter(ArrayUtils::isNotEmpty).map(adds -> AdditionalSearchCriteria.class.cast(adds[0]));
    articles.forEach((uuid, article) -> {
      final ErpInformationType erpInfo = tmcObjectFactory.createErpInformationType();

      erpInfo.setGuid(uuid);
      erpInfo.setRequestedQuantity(BigDecimal.valueOf(article.getAmountNumber()));

      // need to confirm about ConfirmedQuantity
      erpInfo.setConfirmedQuantity(BigDecimal.ZERO);

      final EntityLinkType item = tmcObjectFactory.createEntityLinkType();
      item.setGuid(uuid);
      item.setType(EMasterDataTypeType.ARTICLE_TMF);

      erpInfo.setItem(tmcObjectFactory.createErpInformationTypeItem(item));

      // Create memo request
      additional.map(arrayOfMemoTypeConverter(uuid, article.getGaId()))
      .map(tmcObjectFactory::createArrayOfMemo)
      .ifPresent(erpInfo::setMemos);

      arrayOfErpInfo.getErpInformation().add(erpInfo);
    });

    return arrayOfErpInfo;
  }

  private Function<AdditionalSearchCriteria, ArrayOfMemoType> arrayOfMemoTypeConverter(
      String uuid, String gaId) {
    return criteria -> {
      final ArrayOfMemoType arrayOfMemoType = tmcObjectFactory.createArrayOfMemoType();
      final MemoType memoType = tmcObjectFactory.createMemoType();
      String memoInfo = buildMemoLabelString(gaId, criteria);
      memoType.setGuid(uuid);
      memoType.setType(TYPE_OF_TMC_LABEL_MEMO);
      memoType.setLabel(tmcObjectFactory.createMemoTypeLabel(memoInfo));
      memoType.setText(tmcObjectFactory.createMemoTypeText(memoInfo));
      arrayOfMemoType.getMemo().add(memoType);
      return arrayOfMemoType;
    };
  }

  private static String buildMemoLabelString(String gaId, AdditionalSearchCriteria criteria) {
    final StringBuilder memoStrBuilder = new StringBuilder();

    // SourceId
    Optional.ofNullable(criteria.isDetailArticleRequest())
    .map(MemoArticleSourceIdEnum::valueOf)
    .map(value -> String.format("<SourceId>%s</SourceId>", value.getValue()))
    .ifPresent(memoStrBuilder::append);

    // GenArtNr
    Optional.ofNullable(gaId)
    .filter(StringUtils::isNotBlank)
    .map(id -> String.format("<GenArtNr>%s</GenArtNr>", id))
    .ifPresent(memoStrBuilder::append);

    // KtypNr
    Optional.ofNullable(criteria.getKTypeNr())
    .filter(StringUtils::isNotBlank)
    .map(kTypeNr -> String.format("<KTypNr>%s</KTypNr>", kTypeNr))
    .ifPresent(memoStrBuilder::append);

    // Searchstring
    Optional.ofNullable(criteria.getSearchString())
    .filter(StringUtils::isNotBlank)
    .map(searchStr -> String.format("<Searchstring>%s</Searchstring>", searchStr))
    .ifPresent(memoStrBuilder::append);
    return memoStrBuilder.toString();
  }

}
