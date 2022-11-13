package com.sagag.services.ax.client;

import com.sagag.services.ax.domain.AxReleaseNoteResourceSupport;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.RequestContextUtils;
import com.sagag.services.common.utils.SagJSONUtil;
import java.net.URI;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Slf4j
public class AxBaseClient {

  private static final String API_RELEASE_NOTE = "/webshop-service/release";

  /** Common constants. */
  private static final String BEARER_AUTH_TYPE = "Bearer ";

  /** Common messages for validation. */
  protected static final String COMPANY_NAME_BLANK_MESSAGE =
      "The given company name must not be empty";

  protected static final String CUSTOMER_NUMBER_BLANK_MESSAGE =
      "The given customer number must not be empty";

  protected static final String BASKET_POSITIONS_EMPTY_MESSAGE =
    "The basket positions must not be empty";

  protected static final String DOCUMENT_NUMBER_BLANK_MESSAGE =
      "The given document number must not be empty";

  protected static final String PAYMENT_METHOD_BLANK_MESSAGE =
      "The payment method must not be empty";

  protected static final String SORTING_CRITERIA_BLANK_MESSAGE =
      "The sorting criteria must not be empty";

  @Value("${external.webservice.ax.url}")
  private String url;

  @Autowired
  private AxExchangeClient exchangeClient;

  protected <T> ResponseEntity<T> exchange(String url, HttpMethod method,
    HttpEntity<?> httpEntity, Class<T> responseClazz) {
    // Processes that communicate with the external world,
    // or make remote calls should be logged in production.
    // most of the log is debug. the legal logfaces need business log to
    // external system for evidence just in case and trace if there are so many
    // redundant access to ERP AX API even in prod
    log.info("Ax request: URL = {} ", url);
    commonLogging(httpEntity);
    final ResponseEntity<T> responseEntity =
      exchangeClient.exchange(url, method, httpEntity, responseClazz);
    log.info("Body Response: \n{}", SagJSONUtil.convertObjectToPrettyJson(responseEntity.getBody()));

    return responseEntity;
  }

  protected <T> ResponseEntity<T> exchangeByURI(URI url, HttpMethod method,
      HttpEntity<?> httpEntity, Class<T> responseClazz) {
      // Processes that communicate with the external world,
      // or make remote calls should be logged in production.
      // most of the log is debug. the legal logfaces need business log to
      // external system for evidence just in case and trace if there are so many
      // redundant access to ERP AX API even in prod
      log.info("Ax request: URL = {} ", url.toString());
      commonLogging(httpEntity);
      final ResponseEntity<T> responseEntity =
        exchangeClient.exchangeByURI(url, method, httpEntity, responseClazz);
      log.info("Body Response: \n{}", SagJSONUtil.convertObjectToPrettyJson(responseEntity.getBody()));

      return responseEntity;
    }

  private void commonLogging(HttpEntity<?> httpEntity) {
    log.debug("Headers request: \n{}",
      SagJSONUtil.convertObjectToPrettyJson(httpEntity.getHeaders()));
    final Object httpEntityBody = httpEntity.getBody();
    if (Objects.nonNull(httpEntityBody)) {
      log.info("Body request: \n{}", SagJSONUtil.convertObjectToPrettyJson(httpEntityBody));
    }
  }

  /**
   * Return HTTP entity has body request with access token without body.
   *
   * @return the object of {@link HttpEntity}
   */
  protected static <T> HttpEntity<T> toHttpEntityNoBody(String accessToken) {
    return toHttpEntity(accessToken, null);
  }

  /**
   * Return HTTP entity has body request with access token.
   *
   * @param body the request body
   * @return the object of {@link HttpEntity}
   */
  protected static <T> HttpEntity<T> toHttpEntity(String accessToken, T body) {
    final HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    httpHeaders.add(HttpHeaders.AUTHORIZATION, BEARER_AUTH_TYPE + accessToken);

    final String headerXSagRequestId = RequestContextUtils.getRequestId();
    if (StringUtils.isNotBlank(headerXSagRequestId)) {
      httpHeaders.add(SagConstants.HEADER_X_SAG_REQUEST_ID, headerXSagRequestId);
    } else {
      log.debug("Cannot get requestId for a call to AX");
    }

    if (Objects.isNull(body)) {
      return new HttpEntity<>(httpHeaders);
    }
    return new HttpEntity<>(body, httpHeaders);
  }

  protected String toUrl(String apiPath) {
    return url + apiPath;
  }

  protected String toUrl(String apiPath, Object... params) {
    return String.format(toUrl(apiPath), params);
  }

  /**
   * <p>
   * Public endpoint returns release, build and version information of running service.
   * This public endpoint is the only one, that is not protected any security measurement,
   * and can therefore used as health-check by any monitoring application.
   * </p>
   *
   * <pre>
   * This is a public API to check release info from ERP by AX Platform.
   * Without access token
   * </pre>
   *
   * @return the response of {@link com.sagag.services.ax.domain.AxReleaseNoteResourceSupport}
   */
  public ResponseEntity<AxReleaseNoteResourceSupport> getReleaseInfo() {
    return exchangeClient.exchange(toUrl(API_RELEASE_NOTE), HttpMethod.GET,
      toHttpEntityNoBody(StringUtils.EMPTY), AxReleaseNoteResourceSupport.class);
  }


}
