package com.sagag.services.ax.api.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;

import com.sagag.services.article.api.ArticleExternalService;
import com.sagag.services.article.api.CustomerExternalService;
import com.sagag.services.article.api.EmployeeExternalService;
import com.sagag.services.article.api.InvoiceExternalService;
import com.sagag.services.article.api.OrderExternalService;
import com.sagag.services.article.api.ReturnExternalOrderService;
import com.sagag.services.article.api.domain.customer.CreditLimitInfo;
import com.sagag.services.article.api.domain.customer.CustomerInfo;
import com.sagag.services.article.api.domain.vendor.VendorDto;
import com.sagag.services.article.api.domain.vendor.VendorStockDto;
import com.sagag.services.article.api.request.AvailabilityRequest;
import com.sagag.services.article.api.request.BasketPosition;
import com.sagag.services.article.api.request.InvoiceExternalSearchRequest;
import com.sagag.services.ax.AxApplication;
import com.sagag.services.ax.AxDataTestUtils;
import com.sagag.services.ax.exception.AxCustomerException;
import com.sagag.services.ax.request.AxOrderRequest;
import com.sagag.services.ax.request.AxPriceRequest;
import com.sagag.services.ax.utils.AxConstants;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.enums.order.OrderType;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.domain.sag.erp.ArticleStock;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.domain.sag.erp.OrderConfirmation;
import com.sagag.services.domain.sag.erp.PriceWithArticle;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.domain.sag.external.CustomerBranch;
import com.sagag.services.domain.sag.external.Employee;
import com.sagag.services.domain.sag.invoice.InvoiceDto;
import com.sagag.services.domain.sag.invoice.InvoicePositionDto;
import com.sagag.services.domain.sag.returnorder.TransactionReferenceDto;

import lombok.extern.slf4j.Slf4j;

/**
 * Class to verify AX service in real time.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { AxApplication.class })
@EshopIntegrationTest
@Slf4j
public class AxServiceImplIT {

  @Autowired
  private ArticleExternalService articleExtService;

  @Autowired
  private InvoiceExternalService axInvoiceService;

  @Autowired
  private CustomerExternalService axCustService;

  @Autowired
  private ReturnExternalOrderService axReturnOrderService;

  @Autowired
  private OrderExternalService orderExtService;

  @Autowired
  private EmployeeExternalService employeeExtService;

  @Test
  public void testFindCustomerByNumber() throws Exception {
    Optional<Customer> customer = axCustService.findCustomerByNumber(
        AxDataTestUtils.companyNameOfDDAT(), AxDataTestUtils.customerNr());

    log.debug("Result object : {}", customer.get());

    Assert.assertThat(customer.isPresent(), Matchers.is(true));
    Assert.assertThat(customer.get().getNr(),
        Matchers.is(AxDataTestUtils.customerNr()));
  }

  @Test
  @Ignore("Ax service return error : 500 Internal Server Error")
  public void testFindCustomerAddressById() throws Exception {
    Optional<Address> address =
        axCustService.findCustomerAddressById(AxDataTestUtils.companyNameOfDDAT(),
            AxDataTestUtils.customerNr(), AxDataTestUtils.addressId());

    log.debug("Result object : {}", address.get());

    Assert.assertThat(address.isPresent(), Matchers.equalTo(true));
  }

  @Test
  public void testSearchPrices() throws Exception {

    Map<String, PriceWithArticle> pricesRes = articleExtService
        .searchPrices(AxDataTestUtils.companyNameOfDDAT(), AxDataTestUtils.priceRequest(), false);

    log.debug("Result object : {}", pricesRes);

    Assert.assertThat(pricesRes.size(), Matchers.greaterThan(0));
    Assert.assertThat(pricesRes.containsKey("1000740841"), Matchers.equalTo(true));
  }

  @Test
  public void testSearchAvailabilitiesPICKUP() throws Exception {
    Map<String, List<Availability>> avalabilitiesRes =
        articleExtService.searchAvailabilities(AxDataTestUtils.availabilityRequest());

    log.debug("Result object : {}", avalabilitiesRes);

    Assert.assertThat(avalabilitiesRes.size(), Matchers.greaterThan(0));
    Assert.assertThat(avalabilitiesRes.containsKey("1000740841"), Matchers.equalTo(true));
  }

  @Test
  public void testSearchAvailabilitiesTOUR_tourtimetable_notnull() throws Exception {
    AvailabilityRequest availabilityRequest = AxDataTestUtils.availabilityRequest();
    availabilityRequest.setSendMethodCode("TOUR");
    availabilityRequest.setIsTourTimetable(Boolean.TRUE);

    Map<String, List<Availability>> avalabilitiesRes =
        articleExtService.searchAvailabilities(AxDataTestUtils.availabilityRequest());

    log.debug("Result object : {}", avalabilitiesRes);

    Assert.assertThat(avalabilitiesRes.size(), Matchers.greaterThan(0));
    Assert.assertThat(avalabilitiesRes.containsKey("1000740841"), Matchers.equalTo(true));
  }

  @Test
  public void testSearchAvailabilitiesWithPickupSendMethod() throws Exception {
    Map<String, List<Availability>> avalabilitiesRes = articleExtService
        .searchAvailabilities(AxDataTestUtils.availabilityRequestWithPickupSendMethod());

    log.debug("Result object : {}", avalabilitiesRes);

    Assert.assertThat(avalabilitiesRes.size(), Matchers.greaterThan(0));
    Assert.assertThat(avalabilitiesRes.containsKey("1000740841"), Matchers.equalTo(true));
  }

  @Test(expected = HttpClientErrorException.class)
  public void testSearchStocks() throws Exception {
    final List<String> articleIds = Arrays.asList("1000082238");
    Map<String, List<ArticleStock>> articleStocksRes = articleExtService.searchStocks(
        AxDataTestUtils.companyNameOfDDAT(), articleIds, "1026");

    log.debug("Result object : {}", articleStocksRes);

    Assert.assertThat(articleStocksRes.size(), Matchers.greaterThan(0));
    Assert.assertThat(articleStocksRes.containsKey("1000192367"), Matchers.equalTo(false));
    Assert.assertThat(articleStocksRes.containsKey("1000082238"), Matchers.equalTo(true));
  }

  @Test
  public void testCreateOrder() throws Exception {
    final AxOrderRequest request = AxDataTestUtils.orderRequest();
    log.debug("request object : {}", SagJSONUtil.convertObjectToJson(request));
    OrderConfirmation orderResult =
        orderExtService.createOrder(AxDataTestUtils.companyNameOfDDAT(), request);
    Assert.assertNotNull(orderResult);
    log.debug("result object : {}", SagJSONUtil.convertObjectToJson(orderResult));
  }

  @Test
  public void testCreateBaskets() throws Exception {
    final AxOrderRequest request = AxDataTestUtils.orderRequest();
    log.debug("request object : {}", SagJSONUtil.convertObjectToJson(request));
    OrderConfirmation orderResult =
        orderExtService.createBasket(AxDataTestUtils.companyNameOfDDAT(), request);
    Assert.assertNotNull(orderResult);
    log.debug("result object : {}", SagJSONUtil.convertObjectToJson(orderResult));
  }

  @Test
  public void testSearchBranchById() throws Exception {
    final Optional<CustomerBranch> customerBrach = axCustService
        .searchBranchById(AxDataTestUtils.companyNameOfDDAT(), AxDataTestUtils.defaultBranchId());
    Assert.assertThat(customerBrach.isPresent(), Matchers.is(true));
    Assert.assertThat(customerBrach.get().getBranchId(),
        Matchers.equalTo(AxDataTestUtils.defaultBranchId()));
  }

  @Test
  @Ignore("AX Pred is cleaned all emails for sales")
  public void testFindEmployee() {
    String emailAddress = "andreas.kaltenbrunner@derendinger.at";
    Optional<Employee> employee =
        employeeExtService.findEmployee(AxDataTestUtils.companyNameOfDDAT(), emailAddress);
    Assert.assertThat(employee.isPresent(), Matchers.is(true));
  }

  @Test
  public void testGetNextWorkingDateWithNow() throws Exception {
    Optional<Date> nextWrkDate =
        articleExtService.getNextWorkingDate(AxDataTestUtils.companyNameOfDDAT(),
            AxDataTestUtils.defaultBranchId(), DateTime.now().toDate());
    log.info("Result object : {}", nextWrkDate);
    Assert.assertThat(nextWrkDate.isPresent(), Matchers.is(true));
  }

  @Test
  public void testGetNextWorkingDateWithTomorrow() throws Exception {
    Optional<Date> nextWrkDate =
        articleExtService.getNextWorkingDate(AxDataTestUtils.companyNameOfDDAT(),
            AxDataTestUtils.defaultBranchId(), DateTime.now().plusDays(1).toDate());
    log.debug("Result object : {}", nextWrkDate);
    Assert.assertThat(nextWrkDate.isPresent(), Matchers.is(true));
  }

  @Test
  public void testGetNextWorkingDateWithNewYear() throws Exception {

    DateTime newYearDateTime = new DateTime(DateTime.now().getYear() + 1, 1, 1, 0, 0);

    Optional<Date> nextWrkDate =
        articleExtService.getNextWorkingDate(AxDataTestUtils.companyNameOfDDAT(),
            AxDataTestUtils.defaultBranchId(), newYearDateTime.toDate());
    log.debug("Result object : {}", nextWrkDate);
    Assert.assertThat(nextWrkDate.isPresent(), Matchers.is(true));
  }

  @Test
  public void testGetNextWorkingDateWithEndYear() throws Exception {

    DateTime newYearDateTime = new DateTime(DateTime.now().getYear(), 12, 31, 0, 0);

    Optional<Date> nextWrkDate =
        articleExtService.getNextWorkingDate(AxDataTestUtils.companyNameOfDDAT(),
            AxDataTestUtils.defaultBranchId(), newYearDateTime.toDate());
    log.debug("Result object : {}", nextWrkDate);
    Assert.assertThat(nextWrkDate.isPresent(), Matchers.is(true));
  }

  @Test
  public void testGetNextWorkingDateOnSaturday() throws Exception {

    DateTime newYearDateTime = new DateTime(2018, 3, 7, 12, 0);

    Optional<Date> nextWrkDate =
        articleExtService.getNextWorkingDate(AxDataTestUtils.companyNameOfDDAT(),
            AxDataTestUtils.defaultBranchId(), newYearDateTime.toDate());
    log.debug("Result object : {}", nextWrkDate);
    Assert.assertThat(nextWrkDate.isPresent(), Matchers.is(true));
  }

  @Test
  public void testGetNextWorkingDateAtBeforeEasterForAT() throws Exception {

    DateTime newYearDateTime = new DateTime(2018, 3, 30, 0, 0);

    Optional<Date> nextWrkDate =
        articleExtService.getNextWorkingDate(AxDataTestUtils.companyNameOfDDAT(),
            AxDataTestUtils.defaultBranchId(), newYearDateTime.toDate());
    log.debug("Result object : {}", nextWrkDate);
    Assert.assertThat(nextWrkDate.isPresent(), Matchers.is(true));
  }

  @Test
  public void testGetNextWorkingDateWithEpiphanyHolidayForAT() throws Exception {

    DateTime newYearDateTime = new DateTime(2018, 1, 6, 0, 0);

    Optional<Date> nextWrkDate =
        articleExtService.getNextWorkingDate(AxDataTestUtils.companyNameOfDDAT(),
            AxDataTestUtils.defaultBranchId(), newYearDateTime.toDate());
    log.debug("Result object : {}", nextWrkDate);
    Assert.assertThat(nextWrkDate.isPresent(), Matchers.is(true));
  }

  /**
   * Returns the list customer info to verify some potential data issues happen when get customer
   * info.
   *
   */
  @Test
  public void givenCustomerNrs_ShouldReturnEConnectData() throws Exception {
    final String[] customerNrs = new String[] { "1120383", "1126865", "1129426", "1104561" };

    List<Customer> customers = Stream.of(customerNrs)
        .map(nr -> axCustService
            .findCustomerByNumber(AxDataTestUtils.companyNameOfDDAT(), nr)
            .orElse(new Customer()))
        .collect(Collectors.toList());

    for (Customer customer : customers) {
      log.debug(
          "Customer info : nr = {} "
              + "- send_method = {} - invoice_type = {} - payment_method = {}",
          customer.getNr(), customer.getSendMethod(), customer.getInvoiceTypeCode(),
          customer.getCashOrCreditTypeCode());
    }
  }

  @Test
  @Ignore("Currently, just valid with training url")
  public void shouldReturnCustomerNrBelongToDDCH() {
    final String ddchCustomerNr = "100590L";
    Optional<Customer> customerOpt = axCustService
        .findCustomerByNumber(SupportedAffiliate.DERENDINGER_CH.getCompanyName(),
            String.valueOf(ddchCustomerNr));
    Assert.assertThat(customerOpt.isPresent(), Matchers.is(true));
    final Customer ddchCustomer = customerOpt.get();
    Assert.assertThat(ddchCustomer.getNr(), Matchers.equalTo(ddchCustomerNr));
    Assert.assertThat(ddchCustomer.getAffiliateName(),
        Matchers.equalTo(SupportedAffiliate.DERENDINGER_CH.getCompanyName()));
  }

  @Test
  public void givenArticleId_shouldGetArticlePricesWithAddtionalPrices() {
    final AxPriceRequest request = (AxPriceRequest) AxDataTestUtils.priceRequest();
    request.setCustomerNr("1100005");
    BasketPosition basketPosition = AxDataTestUtils.basketPostion();
    basketPosition.setArticleId("1000426311");
    basketPosition.setQuantity(100);
    request.setBasketPositions(Arrays.asList(basketPosition));

    final Map<String, PriceWithArticle> prices =
        articleExtService.searchPrices(AxDataTestUtils.companyNameOfDDAT(), request, false);

    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(prices));
  }

  @Test
  public void givenCustNr_shouldGetCreditLimitInfo() {
    final String compName = SupportedAffiliate.DERENDINGER_AT.getCompanyName();
    final String custNr = "1100005";
    final Optional<CreditLimitInfo> creditLimitInfo =
        axCustService.getCreditLimitInfoByCustomer(compName, custNr);
    Assert.assertThat(creditLimitInfo.isPresent(), Matchers.is(true));
  }

  @Ignore("Because the AX is correct it, so we can not verify the exception")
  @Test(expected = AxCustomerException.class)
  public void verifyReturnActiveCustomerInfo_ButMissingData() {
    final Optional<CustomerInfo> customerInfo =
        axCustService.getActiveCustomerInfo(AxDataTestUtils.companyNameOfDDAT(), "1500784");
    log.debug("result = {}", SagJSONUtil.convertObjectToPrettyJson(customerInfo.get()));
  }

  @Test
  public void verifyReturnActiveCustomerInfo_HavingFGASCustApprovalType() {
    final Optional<CustomerInfo> customerInfo =
        axCustService.getActiveCustomerInfo(AxDataTestUtils.companyNameOfDDAT(), "1130969");
    Assert.assertNotNull(customerInfo.get().getCustomer().getCustApprovalTypes().stream()
        .filter(p -> "F-Gas".equals(p.getApprovalTypeName())).findFirst().orElse(null));
    log.debug("result = {}", SagJSONUtil.convertObjectToPrettyJson(customerInfo.get().getCustomer().getCustApprovalTypes()));
  }

  @Test
  public void testTransactionReferences() {
    final String compName = SupportedAffiliate.DERENDINGER_AT.getCompanyName();
    final String reference = "AU3010014731";
    final List<TransactionReferenceDto> actual =
        axReturnOrderService.searchTransactionReferences(compName, reference);
    Assert.assertThat(actual.size(), Matchers.greaterThanOrEqualTo(1));
    log.debug("result object : {}", SagJSONUtil.convertObjectToJson(actual));
  }

  @Test
  public void testCreateABSOrderWithDirectInvoice() throws Exception {
    final AxOrderRequest request = AxDataTestUtils.orderRequest();
    request.setOrderType(OrderType.ABS.name());
    request.setSendMethodCode(ErpSendMethodEnum.PICKUP.name());
    request.setPaymentMethod(PaymentMethodType.DIRECT_INVOICE.name());
    log.debug("request object : {}", SagJSONUtil.convertObjectToJson(request));
    OrderConfirmation orderResult =
        orderExtService.createOrder(AxDataTestUtils.companyNameOfDDAT(), request);
    Assert.assertNotNull(orderResult);
    log.debug("result object : {}", SagJSONUtil.convertObjectToJson(orderResult));
  }

  @Test
  public void testCreateABSOrderWithRechnung() throws Exception {
    final AxOrderRequest request = AxDataTestUtils.orderRequest();
    request.setOrderType(OrderType.ABS.name());
    request.setSendMethodCode(ErpSendMethodEnum.PICKUP.name());
    request.setPaymentMethod(PaymentMethodType.CREDIT.name());
    log.debug("request object : {}", SagJSONUtil.convertObjectToJson(request));
    OrderConfirmation orderResult =
        orderExtService.createOrder(AxDataTestUtils.companyNameOfDDAT(), request);
    Assert.assertNotNull(orderResult);
    log.debug("result object : {}", SagJSONUtil.convertObjectToJson(orderResult));
  }

  @Test
  public void testCreateSTDOrderWithDirectInvoice() throws Exception {
    final AxOrderRequest request = AxDataTestUtils.orderRequest();
    request.setOrderType(OrderType.STD.name());
    request.setSendMethodCode(ErpSendMethodEnum.PICKUP.name());
    request.setPaymentMethod(PaymentMethodType.DIRECT_INVOICE.name());
    log.debug("request object : {}", SagJSONUtil.convertObjectToJson(request));
    OrderConfirmation orderResult =
        orderExtService.createOrder(AxDataTestUtils.companyNameOfDDAT(), request);
    Assert.assertNotNull(orderResult);
    log.debug("result object : {}", SagJSONUtil.convertObjectToJson(orderResult));
  }

  @Test
  public void testCreateSTDOrderWithRechnung() throws Exception {
    final AxOrderRequest request = AxDataTestUtils.orderRequest();
    request.setOrderType(OrderType.STD.name());
    request.setSendMethodCode(ErpSendMethodEnum.PICKUP.name());
    request.setPaymentMethod(PaymentMethodType.CREDIT.name());
    log.debug("request object : {}", SagJSONUtil.convertObjectToJson(request));
    OrderConfirmation orderResult =
        orderExtService.createOrder(AxDataTestUtils.companyNameOfDDAT(), request);
    Assert.assertNotNull(orderResult);
    log.debug("result object : {}", SagJSONUtil.convertObjectToJson(orderResult));
  }

  @Test
  public void givenCustomerNr_ShouldGetAllInvoices_1111792() {
    final String companyName = AxDataTestUtils.companyNameOfDDAT();
    final String custNr = "1111792";

    final String dateFrom = "2018-08-01T00:00:00.000Z";
    final String dateTo = "2018-08-26T11:59:59.000Z";
    final InvoiceExternalSearchRequest request = InvoiceExternalSearchRequest.builder()
        .dateFrom(dateFrom)
        .dateTo(dateTo)
        .build();

    final List<InvoiceDto> invoices = axInvoiceService.searchInvoices(companyName, custNr, request);
    Assert.assertThat(invoices.size(), Matchers.greaterThanOrEqualTo(1));
  }

  @Test
  public void givenCustomerNr_ShouldGetAllInvoices_1100005() {
    final String companyName = AxDataTestUtils.companyNameOfDDAT();
    final String custNr = "1100005";

    final String dateFrom = "2018-08-01T00:00:00.000Z";
    final String dateTo = "2018-08-26T11:59:59.000Z";
    final InvoiceExternalSearchRequest request = InvoiceExternalSearchRequest.builder()
        .dateFrom(dateFrom)
        .dateTo(dateTo)
        .build();
    request.setDateFrom(dateFrom);
    request.setDateTo(dateTo);

    final List<InvoiceDto> invoices = axInvoiceService.searchInvoices(companyName, custNr, request);
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(invoices));
    Assert.assertThat(invoices.size(), Matchers.greaterThanOrEqualTo(1));
  }
  
  @Test
  public void givenCustomerNrAndInvoiceNr_ShouldGetAllInvoicePositions_1111792() {
    final String companyName = AxDataTestUtils.companyNameOfDDAT();
    final String custNr = "1111792";
    final String invoiceNr = "3001495081";

    final String dateFrom = "2021-11-24T00:00:00.000Z";
    final String dateTo = "2021-12-24T11:59:59.000Z";
    
    final InvoiceExternalSearchRequest request = InvoiceExternalSearchRequest.builder()
        .dateFrom(dateFrom)
        .dateTo(dateTo)
        .build();
    request.setDateFrom(dateFrom);
    request.setDateTo(dateTo);

    final List<InvoiceDto> invoices = axInvoiceService.searchInvoices(companyName, custNr, request);
    final List<InvoicePositionDto> invoicePositions = invoices.stream()
        .filter(element -> invoiceNr.equals(element.getInvoiceNr()))
        .map(InvoiceDto::getPositions)
        .findFirst()
        .orElse(new ArrayList<>());
    
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(invoicePositions));
    Assert.assertThat(invoicePositions.size(), Matchers.greaterThanOrEqualTo(100));
  }

  @Test
  @Ignore("May be AX SWS data is changed")
  public void givenRelativeUrl_ShouldGetInvoicePDFLink() {
    final String compName = AxDataTestUtils.companyNameOfDDAT();
    final String custNr = "1111792";
    final String invoiceNr = "3000111266";

    Optional<String> url = axInvoiceService.getInvoicePdfUrl(compName, custNr, invoiceNr);
    Assert.assertThat(url.isPresent(), Matchers.is(true));
  }

  @Test
  public void shouldReturnInvoiceDetail_3000111266() {
    final String companyName = AxDataTestUtils.companyNameOfDDAT();
    final String custNr = "1111792";
    final String invoiceNr = "3000111266";

    Optional<InvoiceDto> invoice = axInvoiceService.getInvoiceDetail(companyName, custNr, invoiceNr,
        StringUtils.EMPTY, Optional.empty());
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(invoice.get()));
    Assert.assertThat(invoice.isPresent(), Matchers.is(true));
    Assert.assertThat(invoice.get().getPositions().size(), Matchers.greaterThanOrEqualTo(1));
  }

  @Test
  public void shouldReturnInvoiceDetail_3000111266_SimpleMode() {
    final String companyName = AxDataTestUtils.companyNameOfDDAT();
    final String custNr = "1111792";
    final String invoiceNr = "3000111266";

    Optional<InvoiceDto> invoice = axInvoiceService.getInvoiceDetail(companyName, custNr, invoiceNr,
        StringUtils.EMPTY, Optional.empty());
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(invoice.get()));
    Assert.assertThat(invoice.isPresent(), Matchers.is(true));
    Assert.assertThat(invoice.get().getPositions(), Matchers.not(Matchers.empty()));
  }
  
  @Test
  public void shouldReturnInvoiceDetailHavingMoreThan100Positions_1111792() {
    final String companyName = AxDataTestUtils.companyNameOfDDAT();
    final String custNr = "1111792";
    final String invoiceNr = "3001495081";

    Optional<InvoiceDto> invoice = axInvoiceService.getInvoiceDetail(companyName, custNr, invoiceNr,
        StringUtils.EMPTY, Optional.empty());
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(invoice.get()));
    Assert.assertThat(invoice.isPresent(), Matchers.is(true));
    Assert.assertThat(invoice.get().getPositions().size(), Matchers.greaterThanOrEqualTo(100));
  }

  @Test
  public void shouldReturnVendorList() {
    final String companyName = AxDataTestUtils.companyNameOfDDAT();
    final List<String> articleIds = Arrays.asList("1000494876", "1000020807");
    final List<VendorDto> vendors = articleExtService.searchVendors(companyName, articleIds);
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(vendors));
  }

  @Test
  public void shouldReturnVendorStock() {
    final String companyName = AxDataTestUtils.companyNameOfDDAT();
    final String vendorId = "859067";
    final String branchId = AxConstants.DEFAULT_BRANCH_ID;
    final BasketPosition position1 = new BasketPosition();
    position1.setArticleId("1000494876");
    position1.setExternalArticleId("ZFR6T-11G");
    position1.setQuantity(1);

    final BasketPosition position2 = new BasketPosition();
    position2.setArticleId("1000020807");
    position2.setExternalArticleId("ZFR5P-G");
    position2.setQuantity(1);

    Optional<VendorStockDto> vendorStockOpt = articleExtService.searchVendorStock(companyName,
        vendorId, branchId, Arrays.asList(position1, position2));

    vendorStockOpt.ifPresent(
        vendorStock -> log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(vendorStock)));
  }
}
