package com.sagag.services.tools.client;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.ax.AxAddressResourceSupport;
import com.sagag.services.tools.domain.ax.AxCustomer;
import com.sagag.services.tools.support.SagConstants;
import com.sagag.services.tools.support.SagContextHolder;
import com.sagag.services.tools.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

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
 * 2. Copy content from swagger.yaml and patse to swagger editor
 * 3. View the API desciption
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
@OracleProfile
public class AxClient {

  @Value("${external.webservice.ax.url}")
  private String axUri;

  @Autowired
  @Qualifier("axRestTemplate")
  private RestTemplate restTemplate;

  /** Common messages for validation. */
  private static final String COMPANY_NAME_BLANK_MESSAGE = "The given company name must not be empty";

  private static final String CUSTOMER_NUMBER_BLANK_MESSAGE = "The given customer number must not be empty";

  private static final String API_FIND_CUSTOMER_ADDRESSES = "/webshop-service/customers/%s/%s/addresses";

  /** The API Pattern from AX services. */
  private static final String API_FIND_CUSTOMER_BY_NUMBER = "/webshop-service/customers/%s/%s";

  /** Common constants. */
  private static final String BEARER_AUTH_TYPE = "Bearer ";

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
  public ResponseEntity<AxAddressResourceSupport> getAddressesOfCustomer(String accessToken, String companyName, String customerNr) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.hasText(customerNr, CUSTOMER_NUMBER_BLANK_MESSAGE);
    return exchange(toUrl(API_FIND_CUSTOMER_ADDRESSES, companyName, customerNr), HttpMethod.GET, toHttpEntityNoBody(accessToken),
        AxAddressResourceSupport.class);
  }

  private String toUrl(String apiPath, Object... params) {
    return String.format(toUrl(apiPath), params);
  }

  private String toUrl(String apiPath) {
    return axUri + apiPath;
  }

  /**
   * Return HTTP entity has body request with access token without body.
   *
   * @return the object of {@link HttpEntity}
   */
  private static <T> HttpEntity<T> toHttpEntityNoBody(String accessToken) {
    return toHttpEntity(accessToken, null);
  }

  /**
   * Return HTTP entity has body request with access token.
   *
   * @param body the request body
   * @return the object of {@link HttpEntity}
   */
  private static <T> HttpEntity<T> toHttpEntity(String accessToken, T body) {
    final HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    httpHeaders.add(HttpHeaders.AUTHORIZATION, BEARER_AUTH_TYPE + accessToken);

    final String headerXSagRequestId = SagContextHolder.getRequestId();
    if (StringUtils.isNotBlank(headerXSagRequestId)) {
      httpHeaders.add(SagConstants.HEADER_X_SAG_REQUEST_ID, headerXSagRequestId);
    } else {
      log.warn("Cannot get requestId for a call to AX");
    }

    if (Objects.isNull(body)) {
      return new HttpEntity<>(httpHeaders);
    }
    return new HttpEntity<>(body, httpHeaders);
  }

  private <T> ResponseEntity<T> exchange(String url, HttpMethod method,
      HttpEntity<?> httpEntity, Class<T> responseClazz) {
    // Processes that communicate with the external world,
    // or make remote calls should be logged in production.
    // most of the log is debug. the legal logfaces need business log to
    // external system for evidence just in case and trace if there are so many
    // redundant access to ERP AX API even in prod
    log.info("Ax request: URL = {} ", url);
    log.debug("Headers request: \n{}",
        JsonUtils.convertObjectToPrettyJson(httpEntity.getHeaders()));
    final Object httpEntityBody = httpEntity.getBody();
    if (Objects.nonNull(httpEntityBody)) {
      log.info("Body request: \n{}", JsonUtils.convertObjectToPrettyJson(httpEntityBody));
    }
    final long start = System.currentTimeMillis();
    final ResponseEntity<T> responseEntity =
      restTemplate.exchange(url, method, httpEntity, responseClazz);
    log.info("Body Response: \n{}", JsonUtils.convertObjectToPrettyJson(responseEntity.getBody()));

    log.info("Perf:AxClient -> exchange -> Execute AX API in {} ms",
      System.currentTimeMillis() - start);

    return responseEntity;
  }

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

}
