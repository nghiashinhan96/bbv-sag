package com.sagag.services.ax.client;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.util.UriUtils;

import com.sagag.services.article.api.request.FinancialCardDetailRequest;
import com.sagag.services.article.api.request.FinancialCardHistoryRequest;
import com.sagag.services.ax.domain.financialcard.AxFinancialCardAmount;
import com.sagag.services.ax.domain.financialcard.AxFinancialCardDetail;
import com.sagag.services.ax.domain.financialcard.AxFinancialCardHistory;
import com.sagag.services.common.profiles.SbProfile;

import lombok.extern.slf4j.Slf4j;

@Component
@SbProfile
@Slf4j
public class AxFinancialCardClient extends AxBaseClient {

  /** The API Pattern from AX services. */
  private static final String API_FINANCIAL_CARD_DETAIL = "/webshop-service/entries/%s/%s/%s";

  private static final String API_FINANCIAL_CARD_HISTORY = "/webshop-service/entries/%s/%s";

  private static final String API_FINANCIAL_CARD_AMOUNT = "/webshop-service/usedcreditlimit/%s/%s";

  /** Common messages for validation. */
  protected static final String DOCUMENT_NUMBER_BLANK_MESSAGE =
      "The given document number must not be empty";

  /**
   * <p>
   * Retrieves representation of a financial card detail of a customer.
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @param custNr the customer number
   * @param documentNr the document number
   * @param request the financial card detail request
   * @return the response of {@link AxFinancialCardDetail}
   */
  public ResponseEntity<AxFinancialCardDetail> getFinancialCardDetail(String accessToken,
      String companyName, String custNr, String documentNr, FinancialCardDetailRequest request) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.notNull(custNr, CUSTOMER_NUMBER_BLANK_MESSAGE);
    Assert.notNull(documentNr, DOCUMENT_NUMBER_BLANK_MESSAGE);
    Assert.notNull(request, "The given request must not be null");

    // Build query parameters
    documentNr = UriUtils.encodePathSegment(documentNr, StandardCharsets.UTF_8);
    final StringBuilder urlBuilder = new StringBuilder();
    urlBuilder.append(toUrl(API_FINANCIAL_CARD_DETAIL, companyName, custNr, documentNr));
    urlBuilder.append("?paymentMethod=").append(request.getPaymentMethod());
    urlBuilder.append("&documentType=").append(request.getDocumentType());
    urlBuilder.append("&status=").append(request.getStatus());
    urlBuilder.append("&page=").append(defaultPageOfFinancialCard(request.getPage()));
    try {
      URL url = new URL(urlBuilder.toString());

      return exchangeByURI(url.toURI(), HttpMethod.GET, toHttpEntityNoBody(accessToken),
          AxFinancialCardDetail.class);
    } catch (MalformedURLException | URISyntaxException ex) {
      log.error(String.format("Invalid AX URL: %s", urlBuilder.toString()), ex);
      return ResponseEntity.badRequest().build();
    }

  }

  /**
   * <p>
   * Retrieves representation of a financial card history of a customer.
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @param custNr the customer number
   * @param request the financial card detail request
   * @return the response of {@link AxFinancialCardHistory}
   */
  public ResponseEntity<AxFinancialCardHistory> getFinancialCardHistory(String accessToken,
      String companyName, String custNr, FinancialCardHistoryRequest request) {

    Assert.notNull(request, "The given request must not be null");
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.hasText(custNr, CUSTOMER_NUMBER_BLANK_MESSAGE);
    Assert.hasText(request.getPaymentMethod(), PAYMENT_METHOD_BLANK_MESSAGE);
    Assert.hasText(request.getSorting(), SORTING_CRITERIA_BLANK_MESSAGE);

    final StringBuilder urlBuilder = new StringBuilder();
    urlBuilder.append(toUrl(API_FINANCIAL_CARD_HISTORY, companyName, custNr));
    urlBuilder.append("?paymentMethod=").append(request.getPaymentMethod());
    urlBuilder.append("&sorting=").append(request.getSorting());
    urlBuilder.append("&page=").append(defaultPageOfFinancialCard(request.getPage()));
    if (StringUtils.isNotEmpty(request.getStatus())) {
      urlBuilder.append("&status=").append(request.getStatus());
    }
    if (StringUtils.isNotEmpty(request.getDateFrom())) {
      urlBuilder.append("&dateFrom=").append(request.getDateFrom());
    }
    if (StringUtils.isNotEmpty(request.getDateTo())) {
      urlBuilder.append("&dateTo=").append(request.getDateTo());
    }
    return exchange(urlBuilder.toString(), HttpMethod.GET, toHttpEntityNoBody(accessToken),
        AxFinancialCardHistory.class);
  }

  private static int defaultPageOfFinancialCard(int page) {
    return page >= 0 ? page + 1 : 1;
  }

  /**
   * <p>
   * Retrieves representation of a financial card amount of a customer.
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @param custNr the customer number
   * @param paymentMethod the payment method
   * @return the response of {@link AxFinancialCardAmount}
   */
  public ResponseEntity<AxFinancialCardAmount> getFinancialCardAmount(String accessToken,
      String companyName, String custNr, String paymentMethod) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.hasText(custNr, CUSTOMER_NUMBER_BLANK_MESSAGE);
    Assert.hasText(paymentMethod, PAYMENT_METHOD_BLANK_MESSAGE);

    final StringBuilder urlBuilder = new StringBuilder();
    urlBuilder.append(toUrl(API_FINANCIAL_CARD_AMOUNT, companyName, custNr));
    urlBuilder.append("?paymentMethod=").append(paymentMethod);

    return exchange(urlBuilder.toString(), HttpMethod.GET, toHttpEntityNoBody(accessToken),
        AxFinancialCardAmount.class);
  }
}
