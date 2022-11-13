package com.sagag.services.tools.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.service.SingleSignOnService;
import com.sagag.services.tools.support.AuthorizationFailure;
import com.sagag.services.tools.support.RefreshTokenTask;
import com.sagag.services.tools.support.RequestBearerTokenTask;
import com.sagag.services.tools.support.RequestRefreshTokenTask;
import com.sagag.services.tools.support.ValidateTokenTask;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * The service to to authorize e-Connect with others SAG services.
 * </p>
 *
 */
@Service
@Slf4j
@OracleProfile
public class SingleSignOnServiceImpl implements SingleSignOnService {

  private static final String TOKEN_URL_NOT_EMPTY_MSG = "tokenUrl must not be empty";
  private static final String CLIENT_ID_NOT_EMPTY_MSG = "clientId must not be empty";
  private static final String USERNAME_NOT_EMPTY_MSG = "username must not be empty";
  private static final String CREDENTIALS_NOT_EMPTY_MSG = "password must not be empty";
  private static final String TOKEN_NOT_EMPTY_MSG = "token must not be empty";
  private static final String CLIENT_SECRET_NOT_EMPTY_MSG = "clientSecret must not be empty";

  @Override
  public String authorize(String tokenUrl, String clientId, String username, String password) {
    long start = System.currentTimeMillis();
    Assert.hasText(tokenUrl, TOKEN_URL_NOT_EMPTY_MSG);
    Assert.hasText(clientId, CLIENT_ID_NOT_EMPTY_MSG);
    Assert.hasText(username, USERNAME_NOT_EMPTY_MSG);
    Assert.hasText(password, CREDENTIALS_NOT_EMPTY_MSG);

    String accessToken = StringUtils.EMPTY;
    try {
      accessToken = RequestBearerTokenTask.getAccessTokenFromUserCredentials(tokenUrl, clientId, username, password);
      log.debug("Perf:SingleSignOnService->authorize->getAccessTokenFromUserCredentials {} ms", System.currentTimeMillis() - start);
      return accessToken;
    } catch (Exception ex) {
      log.error("Error occured while obtaining AX token: ", ex);
      throw new NoSuchElementException("Can not get the access token from AX Services");
    }

  }

  @Override
  public boolean hasToken(String token) {
    return StringUtils.isNotEmpty(token);
  }

  @Override
  public Optional<AuthorizationFailure> tokenStatus(String tokenUrl, String token) {
    long start = System.currentTimeMillis();
    Assert.hasText(tokenUrl, TOKEN_URL_NOT_EMPTY_MSG);
    Assert.hasText(token, TOKEN_NOT_EMPTY_MSG);
    Optional<AuthorizationFailure> authFaiure = ValidateTokenTask.validate(token, tokenUrl);
    log.debug("Perf:SingleSignOnService->tokenStatus->validate {} ms", System.currentTimeMillis() - start);
    return authFaiure;
  }
  
  @Override
  public String refreshToken(String tokenUrl, String username, String password,
      String clientId, String clientSecret) {
    long start = System.currentTimeMillis();
    Assert.hasText(tokenUrl, TOKEN_URL_NOT_EMPTY_MSG);
    Assert.hasText(username, USERNAME_NOT_EMPTY_MSG);
    Assert.hasText(password, CREDENTIALS_NOT_EMPTY_MSG);
    Assert.hasText(clientId, CLIENT_ID_NOT_EMPTY_MSG);
    Assert.hasText(clientSecret, CLIENT_SECRET_NOT_EMPTY_MSG);
    try {
      String validRefreshToken =
          RequestRefreshTokenTask.getAccessAndRefreshTokenFromUserCredentials(tokenUrl,
              clientId, username, password);
      log.debug("Perf:SingleSignOnService->refreshToken->getAccessAndRefreshTokenFromUserCredentials {} ms",
          System.currentTimeMillis() - start);

      // Refresh token
      start = System.currentTimeMillis();
      String token = RefreshTokenTask.refreshToken(validRefreshToken, clientId, clientSecret);
      log.debug("Perf:SingleSignOnService->refreshToken->refreshToken {} ms",
          System.currentTimeMillis() - start);
      return token;
    } catch (Exception ex) {
      log.error("Error occured while obtaining AX token: ", ex);
      return authorize(tokenUrl, clientId, username, password);
    }
  }

}
