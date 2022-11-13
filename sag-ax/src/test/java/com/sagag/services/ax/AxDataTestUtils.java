package com.sagag.services.ax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.common.collect.Lists;
import com.sagag.services.domain.eshop.dto.TourTimeDto;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.WorkingHours;
import com.sagag.services.article.api.request.ArticleRequest;
import com.sagag.services.article.api.request.AvailabilityRequest;
import com.sagag.services.article.api.request.BasketPosition;
import com.sagag.services.article.api.request.BasketPositionRequest;
import com.sagag.services.article.api.request.FinancialCardDetailRequest;
import com.sagag.services.article.api.request.FinancialCardHistoryRequest;
import com.sagag.services.article.api.request.PriceRequest;
import com.sagag.services.article.api.request.VehicleRequest;
import com.sagag.services.ax.enums.AxPaymentMethod;
import com.sagag.services.ax.enums.SendMethod;
import com.sagag.services.ax.request.AxAvailabilityRequest;
import com.sagag.services.ax.request.AxNextWorkingDateRequest;
import com.sagag.services.ax.request.AxOrderRequest;
import com.sagag.services.ax.request.AxPriceRequest;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.enums.PriceDisplayTypeEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.enums.WeekDay;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.NextWorkingDates;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.domain.sag.erp.PriceWithArticle;
import com.sagag.services.domain.sag.erp.PriceWithArticlePrice;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * Class to initialize some data test for AX component.
 *
 */
@UtilityClass
@Slf4j
public final class AxDataTestUtils {

  public static final String PRICE_DDAT_API_URL =
      "/webshop-service/articles/Derendinger-Austria/prices";

  public static final String VIN_LICENSE_ARTICLE_ID = "1";

  public static final String CUSTOMER_NR_MATIK_AT_8000016 = "8000016";

  public static final String CUSTOMER_NR_WINT_SB_200014 = "200014";

  public static final String PAYMENT_METHOD_WHOLESALE = "Wholesale";

  public static final String CUSTOMER_NR_DERENDINGER_AT_1100005 = "1100005";

  public static final String INVOICE_NR = "3000100866";

  public static final String INVOICE_NRS = "3000100866,3000091893,3000127299,3070022185,3000119791";

  public static final int OPEN_HOUR_8 = 8;

  public static final int CLOSE_HOUR_18 = 18;

  public static final int LUNCH_TIME_START_12 = 12;

  public static final int LUNCH_TIME_END_13 = 13;

  public static final String FINANCIAL_CARD_DOCUMENT_NR = "IFBGKAR20/11640";

  public static final DateTimeFormatter dtfOut =
      DateTimeFormat.forPattern(DateUtils.UTC_DATE_PATTERN);

  private static final String FIXED_NEXTWORKING_DATE = "2018-06-26T00:00:00Z";

  public static final String COUNTRY_NAME_AT = "Austria";

  public static String companyNameOfDDAT() {
    return SupportedAffiliate.DERENDINGER_AT.getCompanyName();
  }

  public static String companyNameOfWtSB() {
    return SupportedAffiliate.WINT_SB.getCompanyName();
  }

  public static String companyNameOfMAT() {
    return SupportedAffiliate.MATIK_AT.getCompanyName();
  }

  public static String customerNr() {
    return "1123867";
  }

  public static String customerNr4Invoice() {
    return "1111792";
  }

  public static String orderNr() {
    return "AU3010000003";
  }

  public static String fromDate() {
    return "2017-06-18T00:00:00.000Z";
  }

  public static String toDate() {
    return "2017-07-18T23:59:59.999Z";
  }

  public static String ordersLink() {
    return "/webshop-service/orders/Derendinger-Austria/1100005";
  }

  public static String invoicesLink() {
    return "/webshop-service/invoices/Derendinger-Austria/1111792?dateFrom=2018-05-01T00:00:00Z&dateTo=2018-06-01T00:00:00Z";
  }

  public static String orderLink() {
    return "/webshop-service/orders/Derendinger-Austria/1100005";
  }

  public static String addressId() {
    return "2000000000021846720";
  }

  public static String addressDetailUrl() {
    return "/webshop-service/customers/Derendinger-Austria/1100005/addresses/2000000000021846720";
  }

  public static String defaultBranchId() {
    return "1001";
  }

  public static AxOrderRequest orderRequest() {
    List<BasketPositionRequest> items = new ArrayList<>();
    ArticleRequest articleRequest = new ArticleRequest("1000055887", 2);
    articleRequest.setAdditionalTextDoc("additional text doc");
    articleRequest.setRegistrationDocNr("");
    List<ArticleRequest> articles = new ArrayList<>();
    articles.add(articleRequest);
    VehicleRequest vehicleRequest =
        new VehicleRequest(0L, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);
    Optional<VehicleRequest> vehicle = Optional.of(vehicleRequest);
    BasketPositionRequest item1 = new BasketPositionRequest(articles, vehicle);
    items.add(item1);
    AxOrderRequest axOrderRequest = new AxOrderRequest(CUSTOMER_NR_DERENDINGER_AT_1100005, items);

    axOrderRequest.setSendMethodCode(SendMethod.TOUR.name());
    axOrderRequest.setPartialDelivery(true);
    axOrderRequest.setPaymentMethod(AxPaymentMethod.CARD.name());
    axOrderRequest.setDeliveryAddressId("2000000000021846720");
    axOrderRequest.setPickupBranchId("1001");
    axOrderRequest.setPickupBranchAbbreviation(null);
    axOrderRequest.setInvoiceAddressId("2000000000021846720");
    axOrderRequest.setCustomerRefText("customer ref text");
    axOrderRequest.setMessage(StringUtils.EMPTY);
    axOrderRequest.setSingleInvoice(true);
    axOrderRequest.setPersonalNumber("5117");
    axOrderRequest.setSalesOriginId(SupportedAffiliate.DERENDINGER_AT.getSalesOriginId());
    return axOrderRequest;
  }

  public static PriceRequest priceRequest() {
    AxPriceRequest request = new AxPriceRequest();
    request.setGrossPrice(true);
    request.setCustomerNr(CUSTOMER_NR_DERENDINGER_AT_1100005);
    request.setSpecialNetPriceArticleGroup(true);
    request.setBasketPositions(Arrays.asList(basketPostion()));
    request.setPriceTypeDisplayEnum(PriceDisplayTypeEnum.UVPE_OEP_GROSS);
    return request;
  }

  public static PriceRequest licensePriceRequest(String articleId, int quantity) {
    AxPriceRequest request = new AxPriceRequest();
    request.setGrossPrice(true);
    request.setCustomerNr(CUSTOMER_NR_DERENDINGER_AT_1100005);
    request.setSpecialNetPriceArticleGroup(true);
    request.setBasketPositions(Arrays.asList(licenseBasketPosition(articleId, quantity)));
    return request;
  }

  public static BasketPosition licenseBasketPosition(String articleId, int quantity) {
    BasketPosition basketPostion = new BasketPosition();
    basketPostion.setArticleId(articleId);
    basketPostion.setQuantity(quantity);
    return basketPostion;
  }

  public static AvailabilityRequest availabilityRequest() {
    // /webshop-service/articles/Derendinger-Austria/availabilities

    AxAvailabilityRequest request = new AxAvailabilityRequest();
    request.setAvailabilityUrl("/webshop-service/articles/Derendinger-Austria/availabilities");
    request.setCustomerNr(CUSTOMER_NR_DERENDINGER_AT_1100005);
    request.setDeliveryAddressId("2000000000021846720");
    // request.setSendMethodCode(SendMethod.ABH.name());
    request.setSendMethodCode("TOUR");

    request.setCalcSpecificArticleIds(false);
    request.setIncludeBackOrderTime(true); // IMPORTANT
    request.setPartialDelivery(false);
    request.setIsTourTimetable(true);
    request.setPickupBranchId("1001");
    request.setBasketPositions(Arrays.asList(basketPostion()));
    return request;
  }

  public static FinancialCardHistoryRequest financialCardHistoryRequest() {
    return FinancialCardHistoryRequest.builder().paymentMethod("Wholesale").page(1).status("Posted")
        .sorting("PostingDateASC").build();
  }

  public static FinancialCardDetailRequest financialCardDetailRequest() {
    return FinancialCardDetailRequest.builder().paymentMethod("Wholesale").status("Posted")
        .documentType("Invoice").build();
  }

  public static AvailabilityRequest availabilityRequestWithPickupSendMethod() {
    // /webshop-service/articles/Derendinger-Austria/availabilities

    AxAvailabilityRequest request = new AxAvailabilityRequest();
    request.setAvailabilityUrl("/webshop-service/articles/Derendinger-Austria/availabilities");
    request.setCustomerNr(CUSTOMER_NR_DERENDINGER_AT_1100005);
    request.setDeliveryAddressId("2000000000021846720");
    request.setSendMethodCode("PICKUP");
    request.setPickupBranchId("1001");
    request.setCalcSpecificArticleIds(false);
    request.setIncludeBackOrderTime(false);
    request.setPartialDelivery(true);
    request.setBasketPositions(Arrays.asList(basketPostion()));
    return request;
  }

  public static BasketPosition basketPostion() {
    BasketPosition basketPostion = new BasketPosition();
    basketPostion.setArticleId("1000740841");
    basketPostion.setQuantity(1);
    // basketPostion.setBrandId(NumberUtils.createLong("1"));
    return basketPostion;
  }

  public static AxNextWorkingDateRequest nextWorkingDateRequestWithNow() {

    return new AxNextWorkingDateRequest(defaultBranchId(),
        DateTime.now().toString(DateUtils.UTC_DATE_PATTERN));
  }

  public static List<Availability> availabilities() {
    final List<Availability> availabilities = new ArrayList<>();
    Availability avail = new Availability();
    avail.setArticleId("1001097638");
    avail.setQuantity(1);
    avail.setBackOrder(false);
    avail.setArrivalTime(null);
    avail.setAvailState(1);
    avail.setImmediateDelivery(false);
    avail.setStockWarehouse(StringUtils.EMPTY);
    avail.setDeliveryWarehouse(StringUtils.EMPTY);
    avail.setSendMethodCode("ABH");
    avail.setTourTimeTable(Collections.emptyList());
    availabilities.add(avail);
    return availabilities;
  }

  public static List<Availability> availabilities_ImmediateDelivery() {
    final List<Availability> availabilities = new ArrayList<>();
    Availability avail = new Availability();
    avail.setArticleId("1001097638");
    avail.setQuantity(1);
    avail.setBackOrder(true);
    avail.setArrivalTime(null);
    avail.setImmediateDelivery(true);
    avail.setAvailState(1);
    avail.setStockWarehouse(StringUtils.EMPTY);
    avail.setDeliveryWarehouse(StringUtils.EMPTY);
    avail.setSendMethodCode("ABH");
    avail.setTourTimeTable(Collections.emptyList());
    availabilities.add(avail);
    return availabilities;
  }

  public static List<Availability> availabilitiesWithTour() {
    final List<Availability> availabilities = new ArrayList<>();
    Availability avail = new Availability();
    avail.setArticleId("1001097638");
    avail.setQuantity(1);
    avail.setBackOrder(true);
    avail.setArrivalTime(null);
    avail.setImmediateDelivery(false);
    avail.setStockWarehouse(StringUtils.EMPTY);
    avail.setDeliveryWarehouse(StringUtils.EMPTY);
    avail.setSendMethodCode("TOUR");
    avail.setTourTimeTable(Collections.emptyList());
    availabilities.add(avail);
    return availabilities;
  }

  public static ArticleSearchCriteria buildPickupArticleSearchCriteria(
      final NextWorkingDates nextWrkDate) {
    ArticleDocDto article = new ArticleDocDto();
    article.setIdSagsys("1001097638");
    article.setAmountNumber(1);

    ArticleSearchCriteria.ArticleSearchCriteriaBuilder builder = ArticleSearchCriteria.builder()
        .affiliate(SupportedAffiliate.DERENDINGER_AT).custNr("1109480")
        .availabilityUrl("/webshop-service/articles/Derendinger-Austria/availabilities/")
        .partialDelivery(true).deliveryType(ErpSendMethodEnum.PICKUP.name())
        .articles(Arrays.asList(article)).filterArticleBefore(false).updatePrice(false)
        .updateStock(false).addressId(StringUtils.EMPTY).updateAvailability(true).isCartMode(true)
        .nextWorkingDate(nextWrkDate);
    return builder.build();
  }

  public static ArticleSearchCriteria buildTourArticleSearchCriteria(
      final NextWorkingDates nextWrkDate) {
    ArticleDocDto article = new ArticleDocDto();
    article.setIdSagsys("1001097638");
    article.setAmountNumber(1);

    final String branchId = "1001";
    ArticleSearchCriteria.ArticleSearchCriteriaBuilder builder = ArticleSearchCriteria.builder()
        .affiliate(SupportedAffiliate.DERENDINGER_AT).custNr("1118623")
        .availabilityUrl("/webshop-service/articles/Derendinger-Austria/availabilities/")
        .partialDelivery(true).deliveryType(ErpSendMethodEnum.TOUR.name())
        .articles(Arrays.asList(article)).filterArticleBefore(false).updatePrice(false)
        .updateStock(false).addressId(StringUtils.EMPTY).updateAvailability(true).isCartMode(true)
        .nextWorkingDate(nextWrkDate).pickupBranchId(branchId).defaultBrandId(branchId);
    return builder.build();
  }

  public static PriceWithArticle articlePrice() {
    PriceWithArticlePrice articlePrice = new PriceWithArticlePrice();
    articlePrice.setUvpePrice(1.0);
    return PriceWithArticle.builder().price(articlePrice).build();
  }

  public static void logObjects(Object... objects) {
    if (ArrayUtils.isEmpty(objects)) {
      return;
    }
    Stream.of(objects)
        .forEach(obj -> log.info("Result = {}", SagJSONUtil.convertObjectToPrettyJson(obj)));
  }

  public static List<TourTimeDto> getDummyTourTimeList() {
    final List<TourTimeDto> tourTimes = new ArrayList<>();
    tourTimes.add(createTourTime("1001_12345_0800"));
    tourTimes.add(createTourTime("1001_12345_1000"));
    tourTimes.add(createTourTime("1001_12345_1230"));
    tourTimes.add(createTourTime("1001_12345_1430"));
    tourTimes.add(createTourTime("1001_12345_1630"));
    return tourTimes;
  }

  public static TourTimeDto createTourTime(final String tourName) {
    TourTimeDto tourTime = new TourTimeDto();
    tourTime.setTourName(tourName);
    return tourTime;
  }

  public static List<WorkingHours> mockWorkingHours() {
    final WorkingHours workingHour = WorkingHours.builder().openingTime(new LocalTime(OPEN_HOUR_8, 0))
        .closingTime(new LocalTime(CLOSE_HOUR_18, 0))
        .lunchStartTime(new LocalTime(LUNCH_TIME_START_12, 0))
        .lunchEndTime(new LocalTime(LUNCH_TIME_END_13, 0))
        .weekDay(WeekDay.MONDAY)
        .build();
    final List<WorkingHours> workingHours = new ArrayList<>();
    Lists.newArrayList(WeekDay.values()).forEach(weekday -> {
      final WorkingHours wrkingHour = WorkingHours.builder().build();
      SagBeanUtils.copyProperties(workingHour, wrkingHour);
      wrkingHour.setWeekDay(weekday);
      workingHours.add(wrkingHour);
    });

    return workingHours;
  }


  public static NextWorkingDates buildFixedNextWorkingDates() {
    return NextWorkingDates.builder().backorderDate(DateTime.parse(FIXED_NEXTWORKING_DATE).toDate())
        .build();
  }

  public static NextWorkingDates buildNextWorkingDates(String date) {
    return NextWorkingDates.builder().backorderDate(DateTime.parse(date).toDate()).build();
  }

}
