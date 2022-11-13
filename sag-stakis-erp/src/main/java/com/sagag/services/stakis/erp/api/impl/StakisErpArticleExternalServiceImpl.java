package com.sagag.services.stakis.erp.api.impl;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sagag.services.article.api.SoapArticleErpExternalService;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.stakis.erp.client.StakisErpTmConnectClient;
import com.sagag.services.stakis.erp.converter.impl.article.TmGetAvailabilitiesResponseConverterImpl;
import com.sagag.services.stakis.erp.converter.impl.article.TmGetPriceAndStockResponseConverterImpl;
import com.sagag.services.stakis.erp.domain.TmArticlePriceAndAvailabilityRequest;
import com.sagag.services.stakis.erp.domain.TmUserCredentials;
import com.sagag.services.stakis.erp.enums.TmContextId;
import com.sagag.services.stakis.erp.wsdl.tmconnect.GetErpInformationResponseBody;

@Service
@CzProfile
public class StakisErpArticleExternalServiceImpl
    extends StakisProcessor implements SoapArticleErpExternalService {

  @Autowired
  private StakisErpTmConnectClient tmConnectClient;

  @Autowired
  private TmGetAvailabilitiesResponseConverterImpl availabilitiesArticlesConverter;

  @Autowired
  private TmGetPriceAndStockResponseConverterImpl priceAndStockArticlesConverter;

  @Override
  public List<ArticleDocDto> searchArticlePricesAndAvailabilities(String username,
      String customerId, String password, String language, List<ArticleDocDto> articles,
      double vatRate, AdditionalSearchCriteria additional) {
    final TmUserCredentials credentails =
        buildTmUserCredentials(username, password, customerId, language);

    return searchArticlePrices(credentails, articles, vatRate, Optional.empty());
  }

  @Override
  public List<ArticleDocDto> searchArticlePricesAndAvailabilities(String username,
      String customerId, String password, String language, List<ArticleDocDto> articles,
      double vatRate, Optional<VehicleDto> vehicleOpt) {
    final TmUserCredentials credentails =
        buildTmUserCredentials(username, password, customerId, language);

    return searchArticlePrices(credentails, articles, vatRate, vehicleOpt);
  }

  @Override
  public List<ArticleDocDto> searchArticleAvailabilitiesDetails(String username, String customerId,
      String password, String language, List<ArticleDocDto> articles,  double vatRate,
      Optional<VehicleDto> vehicleOpt) {
    final TmUserCredentials credentails =
        buildTmUserCredentials(username, password, customerId, language);

    return searchArticlePricesAndAvailabilities(credentails, articles, vatRate, vehicleOpt, true);
  }

  private List<ArticleDocDto> searchArticlePrices(final TmUserCredentials credentails,
      final List<ArticleDocDto> articles, double vatRate, final Optional<VehicleDto> vehicleOpt) {
    List<ArticleDocDto> articlesWithPrice =
        searchArticlePricesAndAvailabilities(credentails, articles, vatRate, vehicleOpt, false);

    List<ArticleDocDto> articlesWithLowSaleQuantity = articlesWithPrice.stream()
        .filter(lowAmountFilter()).collect(Collectors.toList());
    if (CollectionUtils.isEmpty(articlesWithLowSaleQuantity)) {
      return articlesWithPrice;
    }
    articlesWithLowSaleQuantity.forEach(overrideLowAmount());
    searchArticlePricesAndAvailabilities(
        credentails, articlesWithLowSaleQuantity, vatRate, vehicleOpt, false);

    return articlesWithPrice;
  }

  private List<ArticleDocDto> searchArticlePricesAndAvailabilities(
      final TmUserCredentials credentails, final List<ArticleDocDto> articles, double vatRate,
      final Optional<VehicleDto> vehicleOpt,
      final boolean isAvailDetails) {

    credentails.setContextId(defaultContextId(articles.size(), isAvailDetails).getValue());

    final TmArticlePriceAndAvailabilityRequest request =
        new TmArticlePriceAndAvailabilityRequest(articles, vehicleOpt);

    final GetErpInformationResponseBody resBody =
        tmConnectClient.getERPInformation(credentails, request);

    if (isAvailDetails) {
      return availabilitiesArticlesConverter.apply(articles, resBody, vatRate,
          credentails.getLang());
    }
    return priceAndStockArticlesConverter.apply(articles, resBody, vatRate,
        credentails.getLang());
  }

  private Predicate<ArticleDocDto> lowAmountFilter() {
    return (ArticleDocDto art) -> art.getAmountNumber() < art.getQtyMultiple();
  }

  private Consumer<ArticleDocDto> overrideLowAmount() {
    return (ArticleDocDto art) -> art.setAmountNumber(art.getQtyMultiple());
  }

  private static TmContextId defaultContextId(int sizeOfArticles, boolean isAvailDetails) {
    if (isAvailDetails) {
      return TmContextId.ERP_INFO;
    }
    if (sizeOfArticles == 1) {
      return TmContextId.ARTICLE_DETAIL;
    }
    return TmContextId.ARTICLE_LIST;
  }
}
