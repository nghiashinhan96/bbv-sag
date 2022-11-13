package com.sagag.services.ax.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.common.collect.Lists;
import com.sagag.eshop.repo.entity.SupportedAffiliate;
import com.sagag.services.article.api.availability.ArticleAvailabilityResult;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.request.ArticleRequest;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.common.utils.SagOptional;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.domain.sag.erp.PriceWithArticle;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * Utilities for AX Articles.
 *
 */
@UtilityClass
@Slf4j
public final class AxArticleUtils {

  public static final String OIL_FILTER_GAID = "7";

  private static final Double DEFAULT_PFAND_PRICE = 3.0;

  public static final String PFAND_ART_ID = "9999";

  public static final String CH_AT_ADDITIONAL_TEXT_DOC = "Bel_Position";

  public static final String AXCZ_ADDITIONAL_TEXT_DOC = "ATT_naPozici";

  private static final int MINIMUM_QUANTITY_SHOULD_FIND_ALTERNATIVE_AVAIL = 2;

  /**
   * Equals the pfand article.
   *
   * @param article
   * @return true if its correct, otherwise
   */
  public static boolean equalsPfandArticle(final ArticleDocDto article) {
    return !Objects.isNull(article)
        && StringUtils.equalsIgnoreCase(OIL_FILTER_GAID, article.getGaId());
  }

  /**
   * Returns the default pfand article.
   *
   * @return the price object
   */
  public static PriceWithArticle createPricePfandArticle(final int quantity, final double vatRate) {
    final PriceWithArticle price = PriceWithArticle.empty();
    price.getPrice().setGrossPrice(DEFAULT_PFAND_PRICE);
    price.getPrice().setUvpePrice(DEFAULT_PFAND_PRICE);
    price.getPrice().setNetPrice(DEFAULT_PFAND_PRICE);
    price.getPrice().setTotalGrossPrice(DEFAULT_PFAND_PRICE * quantity);
    price.getPrice().setTotalNetPrice(DEFAULT_PFAND_PRICE * quantity);
    price.getPrice().setGrossPriceWithVat(
        AxPriceUtils.calculatePriceValueWithVat(DEFAULT_PFAND_PRICE, vatRate));
    price.getPrice().setTotalGrossPriceWithVat(
        AxPriceUtils.calculatePriceValueWithVat(price.getPrice().getTotalGrossPrice(), vatRate));
    return price;
  }

  /**
   * Prepares the article list for ERP request.
   *
   * @param articles the article documents
   * @return the list of {@link ArticleRequest}
   */
  public static Collection<ArticleRequest> prepareArticleRequests(
      final Collection<ArticleDocDto> articles) {
    return articles.stream().map(AxArticleUtils::createErpArticleRequest)
        .collect(Collectors.toList());
  }

  /**
   * Returns the article identification object based on the system. (e.g can be umarid,
   * ArtID/SAGsysID, then AXid = PIMid) including it requested quantity.
   *
   * @param article the article documentation from ES.
   * @return the {@link ArticleRequest}
   */
  private static ArticleRequest createErpArticleRequest(final ArticleDocDto article) {
    ArticleRequest articleRequest = new ArticleRequest(findId(article), article.getAmountNumber());
    if (Objects.nonNull(article.getDisplayedPrice())
        && Objects.nonNull(article.getDisplayedPrice().getBrandId())) {
      articleRequest.setBrandId(article.getDisplayedPrice().getBrandId());
    }
    return articleRequest;
  }

  public static ArticleRequest createErpArticleRequest(final String id, final int quantity,
      final String additionalTextDoc, final String sourcingType, final String vendorId,
      final String arrivalTime, SupportedAffiliate supportedAffiliate) {
    final ArticleRequest request = new ArticleRequest(id, quantity);
    request.setSourcingType(sourcingType);
    request.setVendorId(vendorId);
    request.setArrivalTime(arrivalTime);
    if (!StringUtils.isBlank(additionalTextDoc)) {
      request.setAdditionalTextDoc(additionalTextDoc);
      // #1765: AT-AX: Send a text per line of the Order ("Ihre Pos. Referenz")
      // #6372 : Add new value for AXCZ
      request.setAdditionalTextDocPrinters(getAdditionalTextDocPrinters(supportedAffiliate));
    }
    return request;
  }

  private String getAdditionalTextDocPrinters(SupportedAffiliate affiliate) {
    com.sagag.services.common.enums.SupportedAffiliate supportedAffiliate =
        com.sagag.services.common.enums.SupportedAffiliate
            .fromCompanyNameSafely(affiliate.getCompanyName()).orElse(null);
    if (Objects.isNull(supportedAffiliate)) {
      return StringUtils.EMPTY;
    }

    if (supportedAffiliate.isAtAffiliate() || supportedAffiliate.isChAffiliate()) {
      return CH_AT_ADDITIONAL_TEXT_DOC;
    }

    if (supportedAffiliate.isSagCzAffiliate()) {
      return AXCZ_ADDITIONAL_TEXT_DOC;
    }

    return StringUtils.EMPTY;
  }

  public static ArticleRequest createErpArticleRequest(final String id, final int quantity,
      final String additionalTextDoc, SupportedAffiliate supportedAffiliate) {
    return createErpArticleRequest(id, quantity, additionalTextDoc, StringUtils.EMPTY,
        StringUtils.EMPTY, StringUtils.EMPTY, supportedAffiliate);
  }

  private static String findId(final ArticleDocDto article) {

    if (!StringUtils.isBlank(article.getIdSagsys())
        || StringUtils.isNumeric(article.getIdSagsys())) {
      return article.getIdSagsys();
    } else if (!Objects.isNull(article.getArticle())) {
      // For depotArticle and recycleArticle
      return article.getArticle().getId();
    }
    log.error("Not support id for article = {}", article);
    throw new IllegalArgumentException("not suported id for the article doc");
  }

  public Availability initAvailability(ArticleDocDto article, DateTime arrivalTime,
      String deliveryType, Integer quantity) {
    final String arrivalTimeStr =
        DateUtils.getUTCDateString(arrivalTime.toDate(), DateUtils.UTC_DATE_PATTERN);
    return Availability.builder().articleId(article.getArtid()).quantity(quantity)
        .sendMethodCode(deliveryType).arrivalTime(arrivalTimeStr)
        .arrivalTimeAtBranch(arrivalTimeStr).externalSource(true).backOrder(false).build();
  }

  public static List<Availability> defaultAvailabilities(ArticleDocDto article, String deliveryType,
      ArticleAvailabilityResult defaultResult) {
    log.debug("Reset all avail when has no price or no availabilities");
    final Availability availability = new Availability();
    availability.setArticleId(article.getIdSagsys());
    availability.setQuantity(article.getAmountNumber());
    availability.setAvailState(defaultResult.getAvailablityStateCode());
    availability.setAvailStateColor(defaultResult.getAvailablityStateColor());
    availability.setSendMethodCode(getDefaultSendMethodName(deliveryType));

    return Lists.newArrayList(availability);
  }

  private static String getDefaultSendMethodName(final String deliveryType) {
    return StringUtils.isBlank(deliveryType) ? StringUtils.EMPTY
        : ErpSendMethodEnum.valueOf(deliveryType).name();
  }

  public List<ArticleDocDto> updateArticlesAvailability(List<ArticleDocDto> originalArticles,
      List<ArticleDocDto> articleWithAvails) {
    originalArticles.stream().forEach(art -> updateArticleAvailability(art, articleWithAvails));
    return originalArticles;
  }

  private void updateArticleAvailability(ArticleDocDto article,
      List<ArticleDocDto> articleWithAvails) {
    articleWithAvails.stream().filter(art -> StringUtils.equals(article.getArtid(), art.getArtid()))
        .findFirst().ifPresent(art -> article.setAvailabilities(art.getAvailabilities()));
  }

  /**
   * Processes find availabilities of article.
   *
   * @param article
   * @param availabilities
   * @param artCriteria
   * @return the updated article contains availabilities
   */
  public ArticleDocDto findArticleContainsAvailabilities(ArticleDocDto article,
      List<Availability> availabilities, ArticleSearchCriteria artCriteria) {
    int quantity = article.getAmountNumber();
    List<Availability> sortedAvails = sortAvailabilities(availabilities);
    SagOptional.of(findAvailabilityHasEnoughQuantityAndEarliest(quantity, sortedAvails))
        .ifPresent(avail -> article.setAvailabilities(Arrays.asList(avail)))
        .orElse(() -> article.setAvailabilities(
            getSplitPositionAvailabilities(sortedAvails, quantity, article, artCriteria)));

    return article;
  }

  private List<Availability> getSplitPositionAvailabilities(List<Availability> avails,
      int requestedQuantity, ArticleDocDto article, ArticleSearchCriteria artCriteria) {

    int originRequestedQuantity = requestedQuantity;
    List<Availability> originAvails =
        avails.stream().map(SerializationUtils::clone).collect(Collectors.toList());
    List<Availability> topEarliestAxAvails =
        topEarliestAxAvailabilitiesForQuantity(avails, requestedQuantity);

    List<Availability> splitAvails = new ArrayList<>();

    List<Availability> externalAvails = topEarliestAxAvails.stream()
        .filter(Availability::isExternalSource).collect(Collectors.toList());

    if (CollectionUtils.isNotEmpty(externalAvails)) {
      splitAvails.addAll(externalAvails);
      topEarliestAxAvails.removeIf(Availability::isExternalSource);
      originAvails.removeIf(Availability::isExternalSource);
    }

    if (topEarliestAxAvails.size() < MINIMUM_QUANTITY_SHOULD_FIND_ALTERNATIVE_AVAIL) {
      splitAvails.addAll(topEarliestAxAvails);
    } else {
      SagOptional.of(tryToFindAlternativeAvailability(topEarliestAxAvails, originAvails))
          .ifPresent(splitAvails::add).orElse(() -> splitAvails.addAll(topEarliestAxAvails));
    }

    int quantityOfAvailableAvail = splitAvails.stream().mapToInt(Availability::getQuantity).sum();
    int remainingQuantity = originRequestedQuantity - quantityOfAvailableAvail;
    if (remainingQuantity > 0) {
      splitAvails.add(noStockAvailability(Long.valueOf(article.getArtid()), remainingQuantity,
          artCriteria.getDeliveryType()));
    }
    splitAvails.sort(Comparator.comparing(Availability::getCETArrivalTime, sortByArrivalTime()));
    return splitAvails;
  }

  private List<Availability> topEarliestAxAvailabilitiesForQuantity(List<Availability> avails,
      int requestedQuantity) {
    List<Availability> topEarliestAxAvails = new ArrayList<>();
    for (int i = 0; i <= avails.size() - 1 && requestedQuantity > 0; i++) {
      Availability avail = avails.get(i);
      int quantity = avail.getQuantity();
      if (quantity >= requestedQuantity) {
        avail.setQuantity(requestedQuantity);
      }
      topEarliestAxAvails.add(avail);
      requestedQuantity = requestedQuantity - quantity;
    }
    return topEarliestAxAvails;
  }

  private Optional<Availability> tryToFindAlternativeAvailability(
      List<Availability> topEarliestAxAvails, List<Availability> excludedExternalAvails) {
    int quantityOfTopEarliestAxAvails =
        topEarliestAxAvails.stream().mapToInt(Availability::getQuantity).sum();
    return excludedExternalAvails.stream()
        .filter(avail -> avail.getQuantity() >= quantityOfTopEarliestAxAvails).findFirst()
        .map(avail -> {
          avail.setQuantity(quantityOfTopEarliestAxAvails);
          return avail;
        });
  }

  private Optional<Availability> findAvailabilityHasEnoughQuantityAndEarliest(int quantity,
      List<Availability> avails) {
    return avails.stream().filter(avail -> quantity <= avail.getQuantity()).findFirst()
        .map(avail -> {
          avail.setQuantity(quantity);
          return avail;
        });
  }

  private List<Availability> sortAvailabilities(List<Availability> avails) {
    avails.sort(Comparator.comparing(Availability::getCETArrivalTime, sortByArrivalTime())
        .thenComparing(Availability::isExternalSource, sortByExternalSource())
        .thenComparing(Availability::getQuantity, sortByQuantity()));
    return avails;
  }

  private Comparator<DateTime> sortByArrivalTime() {
    return Comparator.naturalOrder();
  }

  private Comparator<Boolean> sortByExternalSource() {
    return Comparator.naturalOrder();
  }

  private Comparator<Integer> sortByQuantity() {
    return Comparator.reverseOrder();
  }

  private Availability noStockAvailability(Long articleId, Integer quantity,
      String sendMethodCode) {
    final Availability availability = new Availability();
    availability.setArticleId(String.valueOf(articleId));
    availability.setQuantity(quantity);
    availability.setBackOrder(true);
    availability.setSendMethodCode(sendMethodCode);
    return availability;
  }

  /**
   * Formats arrival time in UTC at the local openHour.
   *
   * @param dateTime the date time
   * @param openHour the local open hour
   */
  public String toUTCArrivalTime(DateTime dateTime, LocalTime openHour) {
    DateTime localOpenTime = dateTime;
    DateTime openTime = localOpenTime.withTime(openHour);
    if (openTime.isAfter(localOpenTime)) {
      localOpenTime = openTime;
    }
    DateTimeFormatter dtfOut = DateTimeFormat.forPattern(DateUtils.UTC_DATE_PATTERN);
    return dtfOut.withZoneUTC().print(localOpenTime);
  }
  
  public static void distintAvailabilities(List<ArticleDocDto> articles) {
    if (CollectionUtils.isEmpty(articles)) {
      return;
    }
    articles.forEach(art -> {
      if (art.hasAvailabilities()) {
        List<Availability> distinctAvailabilities =
            art.getAvailabilities().stream().distinct().collect(Collectors.toList());
        art.getAvailabilities().clear();
        art.getAvailabilities().addAll(distinctAvailabilities);
      }
    });
  }
}
