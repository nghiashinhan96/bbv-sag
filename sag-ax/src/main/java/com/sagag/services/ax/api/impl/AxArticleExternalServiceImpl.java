package com.sagag.services.ax.api.impl;

import com.sagag.services.article.api.ArticleExternalService;
import com.sagag.services.article.api.domain.vendor.VendorDto;
import com.sagag.services.article.api.domain.vendor.VendorStockDto;
import com.sagag.services.article.api.request.AvailabilityRequest;
import com.sagag.services.article.api.request.BasketPosition;
import com.sagag.services.article.api.request.PriceRequest;
import com.sagag.services.ax.client.AxArticleClient;
import com.sagag.services.ax.converter.AxAvailabilityConverter;
import com.sagag.services.ax.converter.AxVendorStocksConverter;
import com.sagag.services.ax.converter.price.AbstractPriceConverter;
import com.sagag.services.ax.converter.price.PriceConverter;
import com.sagag.services.ax.domain.AxArticlesResourceSupport;
import com.sagag.services.ax.domain.AxAvailabilityResourceSupport;
import com.sagag.services.ax.domain.AxBulkArticleResult;
import com.sagag.services.ax.domain.AxBulkArticleStockResourceSupport;
import com.sagag.services.ax.domain.AxNextWorkingDateResourceSupport;
import com.sagag.services.ax.domain.AxPriceResourceSupport;
import com.sagag.services.ax.domain.AxPriceWithArticle;
import com.sagag.services.ax.domain.AxStock;
import com.sagag.services.ax.domain.vendor.AxVendor;
import com.sagag.services.ax.domain.vendor.AxVendorResouceSupport;
import com.sagag.services.ax.domain.vendor.AxVendorStock;
import com.sagag.services.ax.exception.AxVendorStockNotFoundException;
import com.sagag.services.ax.exception.AxVendorsNotFoundException;
import com.sagag.services.ax.exception.translator.AxExternalExceptionTranslator;
import com.sagag.services.ax.request.AxNextWorkingDateRequest;
import com.sagag.services.ax.request.vendor.AxVendorStockRequest;
import com.sagag.services.ax.translator.AxSendMethodTranslator;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.enums.PriceDisplayTypeEnum;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.sag.erp.ArticleStock;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.domain.sag.erp.BulkArticleResult;
import com.sagag.services.domain.sag.erp.PriceWithArticle;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AxProfile
public class AxArticleExternalServiceImpl extends AxProcessor implements ArticleExternalService {

  private static final String VENDOR_STOCK_ERROR_MSG_PATTERN =
      "Could not find vendor stock with vendorId = %s, branchId = %s, positions = %s";

  @Autowired
  private AxArticleClient axArticleClient;

  @Autowired
  private AxSendMethodTranslator axSendMethodTranslator;

  @Autowired
  private AxAvailabilityConverter axAvailabilityConverter;

  @Autowired
  private List<AbstractPriceConverter> priceConverters;

  @Autowired
  private AxVendorStocksConverter axVendorStockConverter;

  @Autowired
  @Qualifier("axVendorStockExceptionTranslator")
  private AxExternalExceptionTranslator axVendorStockExceptionTranslator;

  @Autowired
  @Qualifier("axVendorsExceptionTranslator")
  private AxExternalExceptionTranslator axVendorsExceptionTranslator;

  @Override
  public Map<String, BulkArticleResult> searchByArticleIds(String compName,
      List<String> articleIds) {
    if (CollectionUtils.isEmpty(articleIds)) {
      return Collections.emptyMap();
    }
    final String artIds = articleIdsRequestParamConverter().apply(articleIds);
    Function<String, ResponseEntity<AxArticlesResourceSupport>> function =
        token -> axArticleClient.getArticles(token, compName, artIds);
    ResponseEntity<AxArticlesResourceSupport> articlesRes = retryIfExpiredToken(function);
    if (!articlesRes.hasBody() || !articlesRes.getBody().hasArticle()) {
      return Collections.emptyMap();
    }
    final Map<String, BulkArticleResult> articles = new HashMap<>();
    articlesRes.getBody().getArticles().forEach(updateArticlesResponse(articles));

    // filter the valid article to requested ref articles
    articleIds.stream().filter(requestedArticleId -> articles.keySet().contains(requestedArticleId))
        .collect(Collectors.toList());
    return articles;
  }

  private static Consumer<AxBulkArticleResult> updateArticlesResponse(
      final Map<String, BulkArticleResult> articles) {
    return validAXArticle -> articles.putIfAbsent(String.valueOf(validAXArticle.getArticleId()),
        validAXArticle.toBulkArticleResult());
  }

  @Override
  public Map<String, BulkArticleResult> searchByUmarIds(String compName, String umarIds) {
    throw new UnsupportedOperationException("The AX system just use article id.");
  }

  @Override
  public Map<String, PriceWithArticle> searchPrices(String companyName, PriceRequest request,
      boolean isFinalCustomerUser) {
    Function<String, ResponseEntity<AxPriceResourceSupport>> function =
        token -> axArticleClient.getArticlePrices(token, companyName, request);
    ResponseEntity<AxPriceResourceSupport> pricesRes = retryIfExpiredToken(function);
    if (!pricesRes.hasBody() || !pricesRes.getBody().hasPrices()) {
      return Collections.emptyMap();
    }

    final PriceConverter validPriceConverter = priceConverters.stream()
        .filter(converter -> converter.isValidConverter(isFinalCustomerUser)).findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Not found compatible price converter"));

    final PriceDisplayTypeEnum priceEnum = request.getPriceTypeDisplayEnum();
    List<AxPriceWithArticle> articlePrices = pricesRes.getBody().getPrices();
    Map<String, PriceWithArticle> articlePriceMap = new HashMap<>();
    for (AxPriceWithArticle articlePrice : articlePrices) {
      articlePriceMap.putIfAbsent(String.valueOf(articlePrice.getArticleId()),
          validPriceConverter.convert(articlePrice, request, priceEnum));
    }
    return articlePriceMap;
  }

  @Override
  public Map<String, List<Availability>> searchAvailabilities(AvailabilityRequest req) {

    // Adapt map PICKUP from e-Connect to Ax ABH
    req.setSendMethodCode(axSendMethodTranslator.translateToAx(req.getSendMethodCode()));
    Function<String, ResponseEntity<AxAvailabilityResourceSupport>> function =
        token -> axArticleClient.getAvailabilities(token, req);
    ResponseEntity<AxAvailabilityResourceSupport> axRes = retryIfExpiredToken(function);
    if (!axRes.hasBody() || !axRes.getBody().hasAvailability()) {
      return Collections.emptyMap();
    }
    return axAvailabilityConverter.convert(axRes.getBody().getAvailabilities());
  }

  @Override
  public Map<String, List<ArticleStock>> searchStocks(String compName, List<String> articleIds,
      final String branchId) {
    if (CollectionUtils.isEmpty(articleIds)) {
      return Collections.emptyMap();
    }

    String artIds = articleIdsRequestParamConverter().apply(articleIds);
    Function<String, ResponseEntity<AxBulkArticleStockResourceSupport>> function =
        token -> axArticleClient.getArticleStocks(token, compName, artIds, branchId);
    ResponseEntity<AxBulkArticleStockResourceSupport> stocksRes = retryIfExpiredToken(function);
    if (!stocksRes.hasBody() || !stocksRes.getBody().hasStocks()) {
      return Collections.emptyMap();
    }

    List<AxStock> positiveStocks = stocksRes.getBody().getStocks().stream()
        .filter(AxStock::hasStock).collect(Collectors.toList());
    Map<String, List<ArticleStock>> stocks = new HashMap<>();
    articleIds.stream().forEach(id -> {
      List<ArticleStock> stockes = positiveStocks.stream()
          .filter(stock -> id.equals(String.valueOf(stock.getArticleId()))).map(item -> {
            ArticleStock result = item.getStock().toStockDto();
            result.setArticleId(id);
            return result;
          }).collect(Collectors.toList());
      stocks.putIfAbsent(id, stockes);
    });
    return stocks;
  }

  @Override
  @LogExecutionTime(
      value = "AxArticleExternalServiceImpl -> Get next of next working date in {} ms")
  public Optional<Date> getNextWorkingDate(String companyName, String branchId, Date requestDate) {
    final AxNextWorkingDateRequest request = new AxNextWorkingDateRequest(branchId,
        new DateTime(requestDate.getTime()).toString(DateUtils.UTC_DATE_PATTERN));
    Function<String, ResponseEntity<AxNextWorkingDateResourceSupport>> function =
        token -> axArticleClient.getNextWorkingDate(token, companyName, request);
    final ResponseEntity<AxNextWorkingDateResourceSupport> nextWrkngDateRes =
        retryIfExpiredToken(function);
    if (!nextWrkngDateRes.hasBody()) {
      return Optional.empty();
    }
    return Optional.of(DateTime.parse(nextWrkngDateRes.getBody().getNextWorkingDate()).toDate());
  }

  @Override
  public List<VendorDto> searchVendors(String companyName, List<String> articleIds) {
    final String artIds = articleIdsRequestParamConverter().apply(articleIds);
    final Function<String, ResponseEntity<AxVendorResouceSupport>> function =
        token -> getOrThrow(() -> axArticleClient.getVendors(token, companyName, artIds),
            axVendorsExceptionTranslator);

    final Supplier<List<VendorDto>> supplier = () -> retryIfExpiredToken(function).getBody()
      .getVendors().stream().map(AxVendor::toDto)
      .collect(Collectors.toList());
    final Supplier<String> errorMsgSupplier =
      () -> String.format("Could not find vendor list with articleIds = %s", articleIds);
    return getDefaultValueIfThrowException(supplier, Collections::emptyList, errorMsgSupplier,
      AxVendorsNotFoundException.class);
  }

  @Override
  public Optional<VendorStockDto> searchVendorStock(String companyName, String vendorId,
      String branchId, List<BasketPosition> positions) {
    final AxVendorStockRequest request = new AxVendorStockRequest(branchId, positions);
    final Function<String, ResponseEntity<AxVendorStock>> function = token -> getOrThrow(
        () -> axArticleClient.getVendorStocks(token, companyName, vendorId, request),
        axVendorStockExceptionTranslator);
    final Supplier<Optional<VendorStockDto>> supplier = () -> {
      final AxVendorStock axVendorStock = retryIfExpiredToken(function).getBody();
      return axVendorStockConverter.apply(axVendorStock, positions);
    };
    final Supplier<String> errorMsgSupplier =
      () -> String.format(VENDOR_STOCK_ERROR_MSG_PATTERN, vendorId, branchId, positions);
    return getDefaultValueIfThrowException(supplier, Optional::empty, errorMsgSupplier,
      AxVendorStockNotFoundException.class);
  }

}
