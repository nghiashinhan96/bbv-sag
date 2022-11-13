package com.sagag.services.ax.client;

import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.article.api.request.InvoiceExternalSearchRequest;
import com.sagag.services.article.api.request.OrderStatusRequest;
import com.sagag.services.article.api.request.returnorder.AxReturnOrderRequest;
import com.sagag.services.ax.domain.AxAddress;
import com.sagag.services.ax.domain.AxAddressResourceSupport;
import com.sagag.services.ax.domain.AxBranch;
import com.sagag.services.ax.domain.AxBranchesResourceSupport;
import com.sagag.services.ax.domain.AxCouriersResourceSupport;
import com.sagag.services.ax.domain.AxCreditLimitInfo;
import com.sagag.services.ax.domain.AxCustomer;
import com.sagag.services.ax.domain.AxOfferConfirmation;
import com.sagag.services.ax.domain.AxOrderConfirmation;
import com.sagag.services.ax.domain.AxOrderHistory;
import com.sagag.services.ax.domain.AxOrderPosition;
import com.sagag.services.ax.domain.AxOrderPositions;
import com.sagag.services.ax.domain.AxOrderStatusConfirmation;
import com.sagag.services.ax.domain.invoice.AxInvoice;
import com.sagag.services.ax.domain.invoice.AxInvoicePDF;
import com.sagag.services.ax.domain.invoice.AxInvoicePositions;
import com.sagag.services.ax.domain.invoice.AxInvoices;
import com.sagag.services.ax.domain.returnorder.AxReturnOrder;
import com.sagag.services.ax.domain.returnorder.AxReturnOrderBatchJobResult;
import com.sagag.services.ax.domain.returnorder.AxReturnOrderBatchJobs;
import com.sagag.services.ax.domain.returnorder.AxTransactionReferences;
import com.sagag.services.ax.request.AxOrderClientRequest;
import com.sagag.services.common.profiles.AxProfile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * <p>
 * The consumer to get response from Ax API.
 * </p>
 *
 * <pre>
 * Build the API consumer base on
 * link: https://bitbucket.sag-ag.ch/projects/SAG-BS/repos/sag-bs-webshop/browse/webshop-service/src/api/swagger.yaml
 * Revision: https://bitbucket.sag-ag.ch/projects/SAG-BS/repos/sag-bs-webshop/commits/e230ba585a1b06b9a3f4575dad1a171287ff6501
 * The guide to view swagger UI
 * 1. Open the URL: https://editor.swagger.io/
 * 2. Copy content from swagger.yaml and paste to swagger editor
 * 3. View the API description
 *
 * Note: We have 2 APIs is un-used, because we also get avail and prices with nested link
 * - private static final String API_FIND_ARTICLE_PRICES = "/webshop-service/articles/%s/prices";
 * - private static final String API_FIND_ARTICLE_AVAILABILITIES =
 *  "/webshop-service/articles/%s/availabilities";
 *
 * Because we re-use nested link in the customer info response
 * </pre>
 *
 */
@Component
@Slf4j
@AxProfile
public class AxClient extends AxBaseClient {

  /** The API Pattern from AX services. */
  private static final String API_FIND_CUSTOMER_BY_NUMBER = "/webshop-service/customers/%s/%s";

  private static final String API_FIND_CUSTOMER_ADDRESSES =
      "/webshop-service/customers/%s/%s/addresses";

  private static final String API_FIND_CUSTOMER_ADDRESS_BY_ID =
      "/webshop-service/customers/%s/%s/addresses/%s";

  private static final String API_CREATE_SHOPPING_BASKET = "/webshop-service/baskets/%s";

  private static final String API_CREATE_ORDER = "/webshop-service/orders/%s";

  private static final String API_CREATE_OFFER = "/webshop-service/offers/%s";

  private static final String API_CREATE_RETURN = "/webshop-service/orders/%s/create-return-order";

  private static final String API_SEARCH_RETURN_ORDER_BATCH_JOB = "/webshop-service/batch-jobs/%s";

  private static final String API_SEARCH_RETURN_ORDER_NUMBER =
      "/webshop-service/orders/%s/journals";

  private static final String API_FIND_ORDERS_BY_CUSTOMER_NR = "/webshop-service/orders/%s/%s";

  private static final String API_FIND_ORDER_ITEMS_BY_ORDER_NR =
      "/webshop-service/orders/%s/%s/%s/positions";

  private static final String API_FIND_ORDER_ITEMS_BY_SEQUENCE =
      "/webshop-service/orders/%s/%s/%s/positions/%s";

  private static final String API_GET_ALL_BRANCHES = "/webshop-service/branches/%s";

  private static final String API_GET_ALL_COURIERS =
      "/webshop-service/customers/%s/courierServices";

  private static final String API_GET_BRANCH_DETAIL_BY_ID = "/webshop-service/branches/%s/%s";

  private static final String API_CREDIT_LIMIT =
      "/webshop-service/customers/%s/%s/creditlimit";

  private static final String API_UPDATE_SALES_ORDER_STATUS =
      "/webshop-service/orders/%s/process-status";

  private static final String API_GET_TRANSACTION_REFERENCES =
      "/webshop-service/transactions/%s?reference=%s";

  private static final String API_SEARCH_INVOICES = "/webshop-service/invoices/%s/%s";

  private static final String API_INVOICE_POSITIONS =
      "/webshop-service/invoices/%s/%s/%s/positions";

  private static final String API_INVOICE_PDF = "/webshop-service/invoices/%s/%s/%s/pdf";

  /** Common messages for validation. */
  private static final String ORDER_NUMBER_BLANK_MESSAGE =
    "The given order number must not be empty";

  private static final String ORDER_LINK_BLANK_MESSAGE =
    "The given order link must not be empty";

  private static final String INVOICE_NUMBER_BLANK_MESSAGE =
      "The given invoice number must not be empty";

  /**
   * <p>
   * Retrieves customer representation by customer number.
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @param customerNr the input customer number
   * @return the response of {@link ResponseEntity<AxCustomer>}
   */
  public ResponseEntity<AxCustomer> getCustomerByNr(final String accessToken,
      final String companyName, final String customerNr) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.hasText(customerNr, CUSTOMER_NUMBER_BLANK_MESSAGE);
    return exchange(
        toUrl(API_FIND_CUSTOMER_BY_NUMBER, companyName, customerNr),
        HttpMethod.GET, toHttpEntityNoBody(accessToken),
        AxCustomer.class);
  }

  /**
   * <p>
   * Retrieves invoice and delivery addresses of the customer.
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @param customerNr the input customer number
   * @return the response of {@link AxAddressResourceSupport}
   */
  public ResponseEntity<AxAddressResourceSupport> getAddressesOfCustomer(
      String accessToken, String companyName, String customerNr) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.hasText(customerNr, CUSTOMER_NUMBER_BLANK_MESSAGE);
    return exchange(
        toUrl(API_FIND_CUSTOMER_ADDRESSES, companyName, customerNr),
        HttpMethod.GET, toHttpEntityNoBody(accessToken),
        AxAddressResourceSupport.class);
  }

  /**
   * <p>
   * Retrieves invoice and delivery addresses of the customer by href link.
   * </p>
   *
   * @param accessToken the ax access token
   * @param addressesUrl the address api
   * @return the response of {@link AxAddressResourceSupport}
   */
  public ResponseEntity<AxAddressResourceSupport> getAddressesOfCustomerByHrefLink(
      String accessToken, String addressesUrl) {
    Assert.hasText(addressesUrl, "The request get the list of addresses must not be empty");
    return exchange(toUrl(addressesUrl),
        HttpMethod.GET, toHttpEntityNoBody(accessToken),
        AxAddressResourceSupport.class);
  }

  /**
   * <p>
   * Retrieves particular address by its id.
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @param customerNr the input customer number
   * @param addressId the input address id
   * @return the response of {@link ResponseEntity<AxAddress>}
   */
  public ResponseEntity<AxAddress> getAddressById(
      String accessToken, String companyName, String customerNr, String addressId) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.hasText(customerNr, CUSTOMER_NUMBER_BLANK_MESSAGE);
    Assert.hasText(addressId, "The request address id must not be empty");
    return exchange(
        toUrl(API_FIND_CUSTOMER_ADDRESS_BY_ID, companyName, customerNr, addressId),
        HttpMethod.GET, toHttpEntityNoBody(accessToken),
        AxAddress.class);
  }

  /**
   * <p>
   * Retrieves particular address by its id by href link.
   * </p>
   *
   * @param accessToken the ax access token
   * @param addressDetailUrl the input address detail api
   * @return the response of {@link ResponseEntity<AxAddress>}
   */
  public ResponseEntity<AxAddress> getAddressByHrefLink(
      String accessToken, String addressDetailUrl) {
    Assert.hasText(addressDetailUrl, "The given address detail url must not be empty");
    return exchange(toUrl(addressDetailUrl),
        HttpMethod.GET, toHttpEntityNoBody(accessToken),
        AxAddress.class);
  }

  /**
   * <p>
   * Requests transfer of new shopping basket to the ERP system.
   * The ERP system asynchronously creates orders based on the shopping basket,
   * but service call immediately returns when shopping basket could be transfered.
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @param orderRequest the input basket request
   * @return the response of {@link AxOrderConfirmation}
   */
  public ResponseEntity<AxOrderConfirmation> createShoppingBasket(
      String accessToken, String companyName, ExternalOrderRequest orderRequest) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.notNull(orderRequest, "The given create basket request must not be null");
    Assert.notEmpty(orderRequest.getBasketPositions(), BASKET_POSITIONS_EMPTY_MESSAGE);

    final AxOrderClientRequest clientRequest = AxOrderClientRequest.of(orderRequest);
    return exchange(toUrl(API_CREATE_SHOPPING_BASKET, companyName),
        HttpMethod.POST, toHttpEntity(accessToken, clientRequest),
        AxOrderConfirmation.class);
  }

  /**
   * <p>
   * Requests forwarding of (new) order to ERP system, so that it can be
   * manually post-processed by AX users.
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @param orderRequest the input order request
   * @return the response of {@link AxOrderConfirmation}
   */
  public ResponseEntity<AxOrderConfirmation> createOrder(String accessToken,
      String companyName, ExternalOrderRequest orderRequest) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.notNull(orderRequest, "The given create order request must not be null");
    Assert.notEmpty(orderRequest.getBasketPositions(), BASKET_POSITIONS_EMPTY_MESSAGE);

    final AxOrderClientRequest clientRequest = AxOrderClientRequest.of(orderRequest);
    return exchange(toUrl(API_CREATE_ORDER, companyName), HttpMethod.POST,
        toHttpEntity(accessToken, clientRequest), AxOrderConfirmation.class);
  }

  /**
   * <p>
   * Requests forwarding of (new) offer to ERP system .
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @param orderRequest the input order request
   * @return the response of {@link AxOrderConfirmation}
   */
  public ResponseEntity<AxOrderConfirmation> createOffer(String accessToken,
      String companyName, ExternalOrderRequest orderRequest) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.notNull(orderRequest, "The given create offer request must not be null");
    Assert.notEmpty(orderRequest.getBasketPositions(), BASKET_POSITIONS_EMPTY_MESSAGE);

    final AxOrderClientRequest clientRequest = AxOrderClientRequest.of(orderRequest);
    log.debug("Create offer request = {}", clientRequest);

    ResponseEntity<AxOfferConfirmation> offerConfirmationEntity =
        exchange(toUrl(API_CREATE_OFFER, companyName), HttpMethod.POST,
            toHttpEntity(accessToken, clientRequest), AxOfferConfirmation.class);
    AxOfferConfirmation offerConfirmationBody = offerConfirmationEntity.getBody();
    AxOrderConfirmation axOrderConfirmation = AxOrderConfirmation.builder()
        .axOrderURL(offerConfirmationBody.getAxOfferURL()).build();
    return ResponseEntity.ok(axOrderConfirmation);

  }

  /**
   * <p>
   * Retrieves representation of a list of orders for a customer.
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @param customerNr the input customer number
   * @param orderNr the input order number
   * @param dateFrom the input date from
   * @param dateTo the input date to
   * @param page the input request page
   * @return the response of {@link AxOrderHistory}
   */
  public ResponseEntity<AxOrderHistory> getOrdersOfCustomer(
      String accessToken, String companyName, String customerNr,
      String orderNr, String dateFrom, String dateTo, Integer page) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.hasText(customerNr, CUSTOMER_NUMBER_BLANK_MESSAGE);

    // Build query parameters
    final StringBuilder urlBuilder = new StringBuilder(toUrl(API_FIND_ORDERS_BY_CUSTOMER_NR,
        companyName, customerNr));
    urlBuilder.append("?page=").append(page);
    if (StringUtils.isNotBlank(orderNr)) {
      urlBuilder.append("&orderNr=").append(orderNr);
    }
    if (StringUtils.isNotBlank(dateFrom)) {
      urlBuilder.append("&dateFrom=").append(dateFrom);
    }
    if (StringUtils.isNotBlank(dateTo)) {
      urlBuilder.append("&dateTo=").append(dateTo);
    }
    return exchange(urlBuilder.toString(),
        HttpMethod.GET, toHttpEntityNoBody(accessToken),
        AxOrderHistory.class);
  }

  /**
   * <p>
   * Retrieves representation of a list of orders for a customer by href link.
   * </p>
   *
   * @param accessToken the ax access token
   * @param ordersLink the input order histories api
   * @return the response of {@link AxOrderHistory}
   */
  public ResponseEntity<AxOrderHistory> getOrdersOfCustomerByHrefLink(String accessToken,
      String ordersLink) {
    Assert.hasText(ordersLink, ORDER_LINK_BLANK_MESSAGE);
    return exchange(toUrl(ordersLink), HttpMethod.GET, toHttpEntityNoBody(accessToken),
        AxOrderHistory.class);
  }

  /**
   * <p>
   * Retrieves the representation of a list of positions for an order given by its number.
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @param customerNr the input customer number
   * @param orderNr the input order number
   * @return the response of {@link AxOrderPositions}
   */
  public ResponseEntity<AxOrderPositions> getOrderPositions(
      String accessToken, String companyName, String customerNr, String orderNr) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.hasText(customerNr, CUSTOMER_NUMBER_BLANK_MESSAGE);
    Assert.hasText(orderNr, ORDER_NUMBER_BLANK_MESSAGE);

    return exchange(
        toUrl(API_FIND_ORDER_ITEMS_BY_ORDER_NR, companyName, customerNr, orderNr),
        HttpMethod.GET, toHttpEntityNoBody(accessToken),
        AxOrderPositions.class);
  }

  /**
   * <p>
   * Retrieves the representation of a particular order position,
   * which is given by the number of the order and the sequence number of the  position.
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @param customerNr the input customer number
   * @param orderNr the input order number
   * @param sequence the input sequence
   * @return the response of {@link AxOrderPosition}
   */
  public ResponseEntity<AxOrderPosition> getOrderPosition(
      String accessToken, String companyName, String customerNr, String orderNr, Integer sequence) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.hasText(customerNr, CUSTOMER_NUMBER_BLANK_MESSAGE);
    Assert.hasText(orderNr, ORDER_NUMBER_BLANK_MESSAGE);
    Assert.notNull(sequence, "The given sequence must not be null");

    return exchange(
        toUrl(API_FIND_ORDER_ITEMS_BY_SEQUENCE, companyName, customerNr, orderNr, sequence),
        HttpMethod.GET, toHttpEntityNoBody(accessToken),
        AxOrderPosition.class);
  }

  /**
   * <p>
   * Retrieves warehouse information.
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @return the response of {@link AxBranchesResourceSupport}
   */
  public ResponseEntity<AxBranchesResourceSupport> getBranches(String accessToken,
      String companyName) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    return exchange(toUrl(API_GET_ALL_BRANCHES, companyName),
        HttpMethod.GET, toHttpEntityNoBody(accessToken),
        AxBranchesResourceSupport.class);
  }

  /**
   * <p>
   * Retrieves courier informations.
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @return the response of {@link AxCouriersResourceSupport}
   */
  public ResponseEntity<AxCouriersResourceSupport> getCouriers(String accessToken,
      String companyName) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    return exchange(toUrl(API_GET_ALL_COURIERS, companyName),
        HttpMethod.GET, toHttpEntityNoBody(accessToken),
        AxCouriersResourceSupport.class);
  }

  /**
   * <p>
   * Retrieves representation of a warehouse which is given by its id.
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @param branchId the input branch id
   * @return the response of {@link AxBranch}
   */
  public ResponseEntity<AxBranch> getBranchById(String accessToken, String companyName,
      String branchId) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.hasText(branchId, "The given branch id must not be empty");
    return exchange(toUrl(API_GET_BRANCH_DETAIL_BY_ID, companyName, branchId),
        HttpMethod.GET, toHttpEntityNoBody(accessToken), AxBranch.class);
  }

  /**
   * <p>
   * Retrieves creditlimit of given customer.
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @param custNr the customer number
   * @return the response of {@link AxCreditLimitInfo}
   */
  public ResponseEntity<AxCreditLimitInfo> getCreditLimitByCustomerNr(String accessToken,
      String companyName, String custNr) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.notNull(custNr, CUSTOMER_NUMBER_BLANK_MESSAGE);
    return exchange(toUrl(API_CREDIT_LIMIT, companyName, custNr),
        HttpMethod.GET, toHttpEntityNoBody(accessToken),
        AxCreditLimitInfo.class);
  }

  /**
   * <p>
   * Requests change of the sales order status.
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @param request the changed order status request
   * @return the response of {@link AxOrderStatusConfirmation}
   */
  public ResponseEntity<AxOrderStatusConfirmation> updateSalesOrderStatus(String accessToken,
      String companyName, OrderStatusRequest request) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.notNull(request, "The given changed order status request must not be null");
    return exchange(toUrl(API_UPDATE_SALES_ORDER_STATUS, companyName),
        HttpMethod.POST, toHttpEntity(accessToken, request), AxOrderStatusConfirmation.class);
  }

  /**
   * <p>
   * Retrieves the representation of a list of transaction references for a given parameter.
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @param reference The value of this parameter can refer to orderNr or deliveryNoteNr
   *                  or invoiceNr or transaction Id
   * @return the response of {@link AxTransactionReferences}
   */
  public ResponseEntity<AxTransactionReferences> getTransactionReferences(String accessToken,
      String companyName, String reference) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.hasText(reference, "The given reference must not be empty");
    return exchange(toUrl(API_GET_TRANSACTION_REFERENCES, companyName, reference),
        HttpMethod.GET, toHttpEntityNoBody(accessToken),
        AxTransactionReferences.class);
  }

  /**
   * <p>
   * Provides functionality to search return order batch job status.
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @param batchJobIds  The request batch job id to be searched
   * @return the response of {@link AxReturnOrderBatchJobs}
   */
  public ResponseEntity<AxReturnOrderBatchJobs> searchReturnOrderBatchJob(String accessToken,
      String companyName, String batchJobIds) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.hasText(batchJobIds, "Batch job ids must not be blank");
    StringBuilder urlBuilder =
        new StringBuilder(toUrl(API_SEARCH_RETURN_ORDER_BATCH_JOB, companyName));
    urlBuilder.append("?batchJobId=").append(batchJobIds);
    return exchange(urlBuilder.toString(), HttpMethod.GET, toHttpEntityNoBody(accessToken),
        AxReturnOrderBatchJobs.class);
  }

  /**
   * <p>
   * Provides functionality to search return order batch join order numbers.
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @param journalIds  The request journal ids to be searched
   * @return the response of {@link AxReturnOrderBatchJobResult}
   */
  public ResponseEntity<AxReturnOrderBatchJobResult> searchReturnOrderNumber(String accessToken,
      String companyName, String journalIds) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.hasText(journalIds, "Journal ids must not be blank");
    StringBuilder urlBuilder =
        new StringBuilder(toUrl(API_SEARCH_RETURN_ORDER_NUMBER, companyName));
    urlBuilder.append("?journalIds=").append(journalIds);
    final String url = urlBuilder.toString();
    return exchange(url, HttpMethod.GET, toHttpEntityNoBody(accessToken),
        AxReturnOrderBatchJobResult.class);
  }

  /**
   * <p>
   * Provides functionality to create a return order and post it.
   * </p>
   *
   * @param accessToken  the ax access token
   * @param companyName  the company name
   * @param orderRequest The request containing the return order to be created
   * @return the response of {@link AxReturnOrder}
   */
  public ResponseEntity<AxReturnOrder> createReturnOrder(String accessToken, String companyName,
      AxReturnOrderRequest orderRequest) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.notNull(orderRequest, "The given create return request must not be null");
    return exchange(toUrl(API_CREATE_RETURN, companyName), HttpMethod.POST,
        toHttpEntity(accessToken, orderRequest), AxReturnOrder.class);
  }

  /**
   * <p>
   * Retrieves representation of a list of invoices for a customer.
   * At least one query must be used, and the dateFrom/dateTo parameters,
   * should be used as a pair, with a date span of 31 days.
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @param custNr the customer number
   * @param request the invoice search request
   * @return the response of {@link AxInvoices}
   */
  public ResponseEntity<AxInvoices> getInvoices(String accessToken, String companyName,
      String custNr, InvoiceExternalSearchRequest request) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.notNull(custNr, CUSTOMER_NUMBER_BLANK_MESSAGE);
    Assert.notNull(request, "The given search request must not be null");

    // Build query parameters
    final StringBuilder urlBuilder = new StringBuilder();
    urlBuilder.append(toUrl(API_SEARCH_INVOICES, companyName, custNr));
    urlBuilder.append("?dateFrom=").append(request.getDateFrom());
    urlBuilder.append("&dateTo=").append(request.getDateTo());

    return exchange(urlBuilder.toString(), HttpMethod.GET, toHttpEntityNoBody(accessToken),
        AxInvoices.class);
  }

  public ResponseEntity<AxInvoice> getInvoiceByNr(String accessToken, String companyName,
      String custNr, String invoiceNr) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.notNull(custNr, CUSTOMER_NUMBER_BLANK_MESSAGE);
    Assert.hasText(invoiceNr, INVOICE_NUMBER_BLANK_MESSAGE);
    return exchange(toUrl("/webshop-service/invoices/%s/%s/%s", companyName, custNr, invoiceNr),
      HttpMethod.GET, toHttpEntityNoBody(accessToken),
      AxInvoice.class);
  }

  /**
   * <p>
   * Retrieves the representation of a list of positions for an invoice given by its number.
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @param custNr the customer number
   * @param invoiceNr the invoice number
   * @param orderNr the order numer
   * @return the response of {@link AxInvoicePositions}
   */
  public ResponseEntity<AxInvoicePositions> getInvoicePositions(String accessToken,
      String companyName, String custNr, String invoiceNr, String orderNr, int page) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.notNull(custNr, CUSTOMER_NUMBER_BLANK_MESSAGE);
    Assert.hasText(invoiceNr, INVOICE_NUMBER_BLANK_MESSAGE);

    final StringBuilder urlBuilder = new StringBuilder();
    urlBuilder.append(toUrl(API_INVOICE_POSITIONS, companyName, custNr, invoiceNr));
    if (!StringUtils.isBlank(orderNr)) {
      urlBuilder.append("?orderNr=").append(orderNr).append("&");
    } else {
      urlBuilder.append("?");
    }
    urlBuilder.append("page=").append(page);
    return exchange(urlBuilder.toString(),
        HttpMethod.GET, toHttpEntityNoBody(accessToken),
        AxInvoicePositions.class);
  }

  /**
   * <p>
   * Retrieves url that points to the pdf of the given invoice nr.
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @param custNr the customer number
   * @param invoiceNr the invoice number
   * @return the response of {@link com.sagag.services.ax.domain.invoice.AxInvoicePositions}
   */
  public ResponseEntity<AxInvoicePDF> getInvoicesPdf(String accessToken, String companyName,
      String custNr, String invoiceNr) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.notNull(custNr, CUSTOMER_NUMBER_BLANK_MESSAGE);
    Assert.hasText(invoiceNr, INVOICE_NUMBER_BLANK_MESSAGE);
    return exchange(toUrl(API_INVOICE_PDF, companyName, custNr, invoiceNr),
        HttpMethod.GET, toHttpEntityNoBody(accessToken),
        AxInvoicePDF.class);
  }

}
