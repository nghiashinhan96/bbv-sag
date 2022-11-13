package com.sagag.services.rest.controller;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.SagJSONUtil;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

/**
 * Abstract integration tests class.
 */
@Getter
public abstract class AbstractControllerIT {

  protected static final int EXPIRED_TIME = 3600;

  @Autowired
  private WebApplicationContext wac;

  @Setter
  private MockMvc mockMvc;

  @Autowired
  private FilterChainProxy springSecurityFilterChain;

  private static final String URL = "https://ax.sib-services.ch/auth-server/oauth/token";
  private static final String CONNECT_BASIC_AUTH = "Basic ZXNob3Atd2ViOmVzaG9wLXdlYi15enRBaEdwRlc=";
  private static final String PWD_USER_AX_AD_123 = "123";
  private static final String USER_AX_AD = "user.ax.ad";

  public static final String AFFI_TECH = "technomag";
  public static final String AFFI_DEREND = "derendinger-ch";
  public static final String AFFI_DEREND_AT = "derendinger-at";
  protected static final String PARAM_AFFILIATE = "affiliate";

  private static final String SCOPES =
      StringUtils.join(new String[] { "read", "write" }, SagConstants.SPACE);

  private static String accessToken;

  @BeforeClass
  public static void retrieveAccessToken() {
    // to avoid hit URL authentication dev server multiple times during running IT tests.
    // The access token should be taken once and re-use
    final HttpHeaders headers = createHeaders(CONNECT_BASIC_AUTH);
    final String body = createBody(USER_AX_AD, PWD_USER_AX_AD_123, AFFI_DEREND_AT);
    final HttpEntity<String> entity = new HttpEntity<>(body, headers);
    final ResponseEntity<TokenResult> result =
        new RestTemplate().postForEntity(URL, entity, TokenResult.class);
    accessToken = result.getBody().getAccessToken();
  }

  @AfterClass
  public static void releaseResources() {
    accessToken = null;
  }

  private static String token() {
    return accessToken;
  }

  /**
   * Returns bearer authentication header during tests.
   * 
   * @return the access token
   */
  protected static String bearerToken() {
    return "Bearer " + token();
  }

  @Before
  public void initTests() {
    setMockMvc(MockMvcBuilders.webAppContextSetup(getWac())
        .addFilter(getSpringSecurityFilterChain()).build());
  }

  private static String createBody(String username, String password, String affiliate) {
    // @formatter:off
    return new StringBuffer("username=").append(username)
        .append("&password=").append(password)
        .append("&grant_type=").append("password")
        .append("&scope=").append(SCOPES)
        .append("&affiliate=").append(affiliate).toString(); // @formatter:on
  }


  private static HttpHeaders createHeaders(final String authorization) {
    final HttpHeaders params = new HttpHeaders();
    params.add(HttpHeaders.AUTHORIZATION, authorization);
    params.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    return params;
  }

  /**
   * Perform POST request with body content to remote url.
   * 
   * @param url the remote url to make the POST request
   * @param body the request body if any
   * @return the {@link RequestBuilder} instance
   * @throws Exception thrown when the action failed
   */
  protected ResultActions performPost(final String url, final Object... body) throws Exception {
    return mockMvc.perform(buildVerb(url, HttpMethod.POST, body));
  }

  /**
   * Performs GET request with parameters content to remote url.
   * 
   * @param url the remote url to make the GET request
   * @return the {@link RequestBuilder} instance
   * @throws Exception thrown when the action failed
   */
  protected ResultActions performGet(final String url) throws Exception {
    return mockMvc.perform(buildVerb(url, HttpMethod.GET));
  }

  /**
   * Perform PUT request with body content to remote url.
   * 
   * @param url the remote url to make the POST request
   * @param body the request body if any
   * @return the {@link RequestBuilder} instance
   * @throws Exception thrown when the action failed
   */
  protected ResultActions performPut(final String url, final Object... body) throws Exception {
    return mockMvc.perform(buildVerb(url, HttpMethod.PUT, body));
  }

  private static RequestBuilder buildVerb(final String url, HttpMethod method,
      final Object... body) {
    final MockHttpServletRequestBuilder request = build(url, method);
    request.accept(MediaType.APPLICATION_JSON_UTF8);
    request.header(HttpHeaders.AUTHORIZATION, bearerToken());
    if (body != null) {
      request.contentType(MediaType.APPLICATION_JSON);
      request.content(SagJSONUtil.convertObjectToJson(body));
    }
    return request;
  }

  private static MockHttpServletRequestBuilder build(String url, HttpMethod method) {
    switch (method) {
      case POST:
        return post(url);
      case PUT:
        return put(url);
      case GET:
        return get(url);
      default:
        throw new UnsupportedOperationException("Not supported request for method: " + method);
    }
  }

}
