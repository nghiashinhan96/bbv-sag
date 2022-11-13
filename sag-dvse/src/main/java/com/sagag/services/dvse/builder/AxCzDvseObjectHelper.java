package com.sagag.services.dvse.builder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.domain.sag.erp.PriceWithArticlePrice;
import com.sagag.services.dvse.dto.dvse.ConnectUser;
import com.sagag.services.dvse.enums.DvseAxCzAvailabilityState;
import com.sagag.services.dvse.wsdl.tmconnect.ArrayOfArticleTmf;
import com.sagag.services.dvse.wsdl.tmconnect.ArrayOfAvailabilityState;
import com.sagag.services.dvse.wsdl.tmconnect.ArrayOfErpInformation;
import com.sagag.services.dvse.wsdl.tmconnect.ArrayOfOrderPosition;
import com.sagag.services.dvse.wsdl.tmconnect.ArrayOfPrice;
import com.sagag.services.dvse.wsdl.tmconnect.ArticleTmf;
import com.sagag.services.dvse.wsdl.tmconnect.AvailabilityState;
import com.sagag.services.dvse.wsdl.tmconnect.EMasterDataType;
import com.sagag.services.dvse.wsdl.tmconnect.EntityLink;
import com.sagag.services.dvse.wsdl.tmconnect.ErpInformation;
import com.sagag.services.dvse.wsdl.tmconnect.GetErpInformation;
import com.sagag.services.dvse.wsdl.tmconnect.GetErpInformationReply;
import com.sagag.services.dvse.wsdl.tmconnect.GetErpInformationResponse;
import com.sagag.services.dvse.wsdl.tmconnect.MasterData;
import com.sagag.services.dvse.wsdl.tmconnect.Order;
import com.sagag.services.dvse.wsdl.tmconnect.OrderCollection;
import com.sagag.services.dvse.wsdl.tmconnect.OrderPosition;
import com.sagag.services.dvse.wsdl.tmconnect.Price;
import com.sagag.services.dvse.wsdl.tmconnect.SendOrder;
import com.sagag.services.dvse.wsdl.tmconnect.SendOrderReply;
import com.sagag.services.dvse.wsdl.tmconnect.SendOrderRequest;
import com.sagag.services.dvse.wsdl.tmconnect.SendOrderResponse;
import com.sagag.services.dvse.wsdl.tmconnect.SupplierTmf;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AxCzDvseObjectHelper {

  private static final String DEFAULT_LANGUAGE_CODE_ISO_639_1 = "cs";

  private static final String DEFAULT_CURRENCY_CODE_CZ = "CZK";

  private static final Double DEFAULT_PRICE = 0.0;

  public static final int GROSS_PRICE = 5;

  public static final int NET_PRICE = 4;

  public static List<ArticleTmf> extractArticleTmfs(Optional<MasterData> request) {
    if (!request.isPresent()) {
      return new ArrayList<>();
    }
    Optional<ArrayOfArticleTmf> articleTmfs = Optional.ofNullable(request.get().getArticleTmfs());
    if (!articleTmfs.isPresent()) {
      return new ArrayList<>();
    }
    return articleTmfs.get().getArticleTmf();
  }

  public static List<ErpInformation> extractErpInformation(GetErpInformation request) {
    if (request == null) {
      return new ArrayList<>();
    }
    final Optional<ArrayOfErpInformation> erpInfo =
        Optional.ofNullable(request.getRequest().getErpArticleInformation());
    if (!erpInfo.isPresent()) {
      return new ArrayList<>();
    }
    return erpInfo.get().getErpInformation();
  }

  public static Map<String, String> extractArticleIdUUIDMap(Optional<MasterData> masterData) {
    if (!masterData.isPresent()) {
      return Collections.emptyMap();
    }
    final Optional<ArrayOfArticleTmf> arrayOfArticleTmfType =
        Optional.ofNullable(masterData.get().getArticleTmfs());
    if (!arrayOfArticleTmfType.isPresent()) {
      return Collections.emptyMap();
    }

    final List<ArticleTmf> articleTmfTypes =
        arrayOfArticleTmfType.map(ArrayOfArticleTmf::getArticleTmf).orElse(Collections.emptyList());
    if (CollectionUtils.isEmpty(articleTmfTypes)) {
      return Collections.emptyMap();
    }

    return articleTmfTypes.stream().filter(item -> Objects.nonNull(item.getArticleIdErp()))
        .collect(Collectors.toMap(
            i -> Optional.ofNullable(i.getArticleIdErp()).orElse(StringUtils.EMPTY),
            ArticleTmf::getGuid, articleIdMerger()));
  }

  public static List<OrderPosition> extractOrderPosition(SendOrder sendOrder) {
    List<OrderPosition> extractedOrderPositions = new ArrayList<>();
    if (!Objects.isNull(sendOrder)) {
      Optional<SendOrderRequest> requestOpt = Optional.ofNullable(sendOrder.getRequest());
      if (requestOpt.isPresent()) {

        List<Order> orders = requestOpt.get().getOrderCollection().getOrders().getOrder();
        extractedOrderPositions = CollectionUtils.emptyIfNull(orders).stream().map(Order::getItems)
            .collect(Collectors.toList()).stream().map(ArrayOfOrderPosition::getOrderPosition)
            .collect(Collectors.toList()).stream().flatMap(List::stream)
            .collect(Collectors.toList());
      }
    }

    return extractedOrderPositions;
  }

  public static SendOrderResponse buildAddToBasketResponse(SendOrder order) {
    Optional<MasterData> masterDataOpt = Optional.ofNullable(order.getRequest().getMasterData());
    Optional<OrderCollection> orderCollectionOpt =
        Optional.ofNullable(order.getRequest().getOrderCollection());
    SendOrderReply reply = new SendOrderReply();
    reply.setMasterData(masterDataOpt.orElse(new MasterData()));
    reply.setOrderCollection(orderCollectionOpt.orElse(new OrderCollection()));

    SendOrderResponse response = new SendOrderResponse();
    response.setSendOrderResult(reply);
    return response;
  }

  public static GetErpInformationResponse buildGetArticleInformationResponse(
      GetErpInformation request, Map<String, String> articleIdAndUUIDMap, List<ArticleTmf> artTmfs,
      List<ErpInformation> erpInfos, final List<ArticleDocDto> updatedArticles, ConnectUser user) {
    ArrayOfAvailabilityState availStates = new ArrayOfAvailabilityState();
    articleIdAndUUIDMap.keySet().forEach(artId -> {

      Optional<ArticleDocDto> updatedArt = updatedArticles.stream()
          .filter(art -> art.getArtid().equalsIgnoreCase(artId)).findFirst();
      Optional<ArticleTmf> artTmfOpt = CollectionUtils.emptyIfNull(artTmfs).stream()
          .filter(art -> art.getArticleIdErp().equalsIgnoreCase(artId)).findFirst();

      if (artTmfOpt.isPresent() && updatedArt.isPresent()) {
        String guid = UUID.randomUUID().toString();
        availStates.getAvailabilityState().add(buildAvailStateWithMultiLanguages(updatedArt.get(),
            guid, Optional.ofNullable(request.getRequest().getLanguageCodeIso6391()).orElse(DEFAULT_LANGUAGE_CODE_ISO_639_1)));

        PriceWithArticlePrice articleRepsonsePrice = updatedArt.get().getPrice().getPrice();

        CollectionUtils.emptyIfNull(erpInfos).forEach(erp -> {
          if (erp.getItem().getGuid().equalsIgnoreCase(artTmfOpt.get().getGuid())) {
            List<Price> prices = buildPrices(articleRepsonsePrice);
            ArrayOfPrice arrayOfPrice = new ArrayOfPrice();
            arrayOfPrice.getPrice().addAll(prices);
            erp.setPrices(arrayOfPrice);

            EntityLink availStateErp = new EntityLink();
            availStateErp.setGuid(guid);
            availStateErp.setType(EMasterDataType.AVAILABILITY_SATE);
            erp.setAvailabilityState(availStateErp);
          }
        });
      }

    });

    MasterData responseMaster = request.getRequest().getMasterData();
    responseMaster.setAvailabilityStates(availStates);
    GetErpInformationReply reply = new GetErpInformationReply();
    reply.setMasterData(responseMaster);
    ArrayOfErpInformation erpInfoArray = new ArrayOfErpInformation();
    erpInfoArray.getErpInformation().addAll(erpInfos);
    reply.setErpArticleInformation(erpInfoArray);

    GetErpInformationResponse response = new GetErpInformationResponse();
    response.setGetErpInformationResult(reply);
    return response;
  }

  public static AvailabilityState buildAvailStateWithMultiLanguages(ArticleDocDto updatedArt,
      String guid, String languageCode639_1) {
    AvailabilityState availState = new AvailabilityState();

    DvseAxCzAvailabilityState foundMappingAvailState = mappingAvailState(updatedArt);
    availState.setType(foundMappingAvailState.getCode());
    availState.setGuid(guid);
    availState.setShortDescription(DvseAxCzAvailabilityState
        .translateAvailStateByLanguageCode(languageCode639_1, foundMappingAvailState));
    return availState;
  }

  public static List<ArticleDocDto> buildArticleFromGetInformationRequest(
      Map<String, String> articleIdAndUUIDMap, List<ArticleTmf> artTmfs,
      List<ErpInformation> erpInfos) {
    List<ArticleDocDto> requestedArticles = new ArrayList<>();
    articleIdAndUUIDMap.keySet().forEach(artId -> {
      String uuid = articleIdAndUUIDMap.get(artId);
      Optional<ArticleTmf> artTmfOpt = findArticleTmfByUUID(uuid, artTmfs);
      Optional<ErpInformation> erpOpt = findErpInformationByUUID(uuid, erpInfos);
      requestedArticles.add(buildRequestedArticle(artTmfOpt,
          Optional.ofNullable(erpOpt.get().getRequestedQuantity())));
    });
    return requestedArticles;
  }

  public static List<ArticleDocDto> buildArticleFromOrderRequest(
      Map<String, String> articleIdAndUUIDMap, List<ArticleTmf> artTmfs,
      List<OrderPosition> orderPosition) {
    List<ArticleDocDto> requestedArticles = new ArrayList<>();
    articleIdAndUUIDMap.keySet().forEach(artId -> {
      String uuid = articleIdAndUUIDMap.get(artId);
      Optional<ArticleTmf> artTmfOpt = findArticleTmfByUUID(uuid, artTmfs);
      Optional<OrderPosition> orderPosOpt = findOrderPositionByUUID(uuid, orderPosition);
      requestedArticles.add(buildRequestedArticle(artTmfOpt,
          Optional.ofNullable(orderPosOpt.get().getRequestedQuantity())));
    });
    return requestedArticles;
  }

  public static List<Price> buildPrices(PriceWithArticlePrice artPrice) {
    List<Price> prices = new ArrayList<>();
    prices.add(createPrice(GROSS_PRICE, artPrice.getGrossPriceWithVat(),
        artPrice.getDiscountInPercent(), artPrice.getVatInPercent(), true, artPrice.getCurrency()));
    prices.add(createPrice(GROSS_PRICE, artPrice.getGrossPrice(), DEFAULT_PRICE, DEFAULT_PRICE,
        false, artPrice.getCurrency()));
    prices.add(createPrice(NET_PRICE, artPrice.getNetPriceWithVat(), DEFAULT_PRICE,
        artPrice.getVatInPercent(), true, artPrice.getCurrency()));
    prices.add(createPrice(NET_PRICE, artPrice.getNetPrice(), DEFAULT_PRICE, DEFAULT_PRICE, false,
        artPrice.getCurrency()));

    return prices;
  }

  private static ArticleDocDto buildRequestedArticle(Optional<ArticleTmf> artTmfOpt,
      Optional<BigDecimal> amount) {
    ArticleDocDto article = new ArticleDocDto();
    if (artTmfOpt.isPresent()) {
      article.setArtid(artTmfOpt.get().getArticleIdErp());
      article.setAmountNumber(
          amount.isPresent() ? amount.get().intValue() : SagConstants.DEFAULT_SALES_QUANTITY);
      Optional<SupplierTmf> supplierOpt = Optional.ofNullable(artTmfOpt.get().getSupplier());
      if (supplierOpt.isPresent()) {
        article.setSupplierId(supplierOpt.get().getSupplierId());
        article.setSupplier(supplierOpt.get().getName());
      }
      article.setSupplierArticleNumber(artTmfOpt.get().getArticleIdSupplier());
      article.setArtnrDisplay(artTmfOpt.get().getArticleIdSupplier());
    }
    return article;
  }

  public static Optional<ArticleTmf> findArticleTmfByUUID(String uuid, List<ArticleTmf> artTmfs) {
    return CollectionUtils.emptyIfNull(artTmfs).stream()
        .filter(art -> StringUtils.equalsIgnoreCase(art.getGuid(), uuid)).findFirst();
  }

  public static Optional<ErpInformation> findErpInformationByUUID(String uuid,
      List<ErpInformation> erpInfos) {

    if (StringUtils.isEmpty(uuid)) {
      return Optional.empty();
    }

    if (CollectionUtils.isEmpty(erpInfos)) {
      return Optional.empty();
    }
    return CollectionUtils.emptyIfNull(erpInfos).stream()
        .filter(
            erp -> Objects.nonNull(erp.getItem()) && Objects.equals(erp.getItem().getGuid(), uuid))
        .findFirst();
  }

  public static Optional<OrderPosition> findOrderPositionByUUID(String uuid,
      List<OrderPosition> orderPositions) {

    if (StringUtils.isEmpty(uuid)) {
      return Optional.empty();
    }

    if (CollectionUtils.isEmpty(orderPositions)) {
      return Optional.empty();
    }
    return CollectionUtils.emptyIfNull(orderPositions).stream()
        .filter(
            erp -> Objects.nonNull(erp.getItem()) && Objects.equals(erp.getItem().getGuid(), uuid))
        .findFirst();
  }

  private Price createPrice(Integer priceType, Double value, Double rebate, Double vatInPercent,
      boolean isTaxIncluded, String currencyCode) {
    Price grossWithVat = new Price();
    grossWithVat.setType(priceType);
    grossWithVat.setValue(BigDecimal.valueOf(Optional.ofNullable(value).orElse(DEFAULT_PRICE)));
    grossWithVat.setRebate(BigDecimal.valueOf(Optional.ofNullable(rebate).orElse(DEFAULT_PRICE)));
    grossWithVat
        .setVAT(BigDecimal.valueOf(Optional.ofNullable(vatInPercent).orElse(DEFAULT_PRICE)));
    grossWithVat.setTaxIncluded(isTaxIncluded);
    grossWithVat
        .setCurrencyCodeIso4217(Optional.ofNullable(currencyCode).orElse(DEFAULT_CURRENCY_CODE_CZ));
    return grossWithVat;
  }

  private DvseAxCzAvailabilityState mappingAvailState(ArticleDocDto updatedArt) {
    boolean atleastHasOneOrderTrue =
        updatedArt.getAvailabilities().stream().anyMatch(Availability::isBackOrderTrue);

    boolean allHasNoArrivalTime = updatedArt.getAvailabilities().stream()
        .allMatch(avail -> Objects.isNull(avail.getArrivalTime()));
    if (updatedArt.hasNoAvailabilities() || Boolean.TRUE.equals(atleastHasOneOrderTrue)
        || Boolean.TRUE.equals(allHasNoArrivalTime)) {
      return DvseAxCzAvailabilityState.RED;
    }

    if (updatedArt.hasAvailabilities() && updatedArt.getAvailabilities().size() > 1
        && updatedArt.getSalesQuantity() > 1) {
      return DvseAxCzAvailabilityState.YELLOW;
    }
    return DvseAxCzAvailabilityState.GREEN;
  }

  private BinaryOperator<String> articleIdMerger() {
    return (artId1, artId2) -> artId2;
  }

}
