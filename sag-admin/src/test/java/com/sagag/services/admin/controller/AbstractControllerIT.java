package com.sagag.services.admin.controller;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.sagag.eshop.service.resource.TokenResult;
import com.sagag.services.common.contants.SagConstants;


@Getter
public abstract class AbstractControllerIT {

  protected static final int EXPIRED_TIME = 3600;

  @Autowired
  private WebApplicationContext wac;

  @Setter
  private MockMvc mockMvc;

  @Autowired
  private FilterChainProxy springSecurityFilterChain;

  public static final String AFFI_TECH = "technomag";

  public static final String AFFI_DEREND = "derendinger-ch";

  protected static final String PARAM_AFFILIATE = "affiliate";

  @Value("${sag.security.auth.host}")
  private String authHost;

  @Value("${sag.security.auth.clientId}")
  private String authClientId;

  @Value("${sag.security.auth.clientSecret}")
  private String authClientSecret;

  @Value("${sag.security.auth.tokenUrl}")
  private String authTokenUrl;

  private static final String SCOPES
      = StringUtils.join(new String[] { "read", "write" }, SagConstants.SPACE);

  @Before
  public void initTests() {
    setMockMvc(MockMvcBuilders.webAppContextSetup(getWac())
        .addFilter(getSpringSecurityFilterChain()).build());
  }

  protected String obtainWebAccessToken(
      final String username, final String password, final String affiliate) throws Exception {
    final String authorization = createBasicAuthorization(authClientId, authClientSecret);
    final HttpHeaders headers = createHeaders(authorization);
    final String body = createBody(
        username, password, affiliate, authClientId, authClientSecret);
    return requestAuthServerAccessToken(headers, body);
  }

  protected String obtainAdminWebAccessToken(
      final String username, final String password, final String affiliate) throws Exception {
    final String adminId = "eshop-admin";
    final String adminSecret = "eshop-admin-WFpGhAtzy";
    final String authorization = createBasicAuthorization(adminId, adminSecret);
    final HttpHeaders headers = createHeaders(authorization);
    final String body = createBody(
        username, password, affiliate, authClientId, authClientSecret);
    return requestAuthServerAccessToken(headers, body);
  }

  private static String createBody(
      String username, String password, String affiliate, String id, String secret) {
    return new StringBuffer("username=")
      .append(username).append("&password=").append(password)
      .append("&grant_type=").append("password")
      .append("&scope=").append(SCOPES)
      .append("&affiliate=").append(affiliate).toString();
  }

  private String requestAuthServerAccessToken(
      final HttpHeaders headers, final String body) throws Exception {
    final HttpEntity<String> entity = new HttpEntity<>(body, headers);
    final ResponseEntity<TokenResult> result
        = new RestTemplate().exchange(
            authHost + authTokenUrl,
            HttpMethod.POST, entity, TokenResult.class);
    return result.getBody().getAccessToken();
  }

  private static HttpHeaders createHeaders(final String authorization) {
    final HttpHeaders params = new HttpHeaders();
    params.add("Authorization", authorization);
    params.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    return params;
  }

  private static String createBasicAuthorization(final String id, final String secret) {
    final String credentials = id + ":" + secret;
    return "Basic " + new String(Base64Utils.encode(credentials.getBytes()));
  }

}
