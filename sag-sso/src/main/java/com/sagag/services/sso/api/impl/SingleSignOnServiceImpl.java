package com.sagag.services.sso.api.impl;

import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.sso.api.SingleSignOnService;
import com.sagag.services.sso.tasks.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * <p>
 * The service to to authorize e-Connect with others SAG services.
 * </p>
 *
 */
@Service
@Slf4j
public class SingleSignOnServiceImpl implements SingleSignOnService {

  private static final String TOKEN_URL_NOT_EMPTY_MSG = "tokenUrl must not be empty";
  private static final String CLIENT_ID_NOT_EMPTY_MSG = "clientId must not be empty";
  private static final String CLIENT_SECRET_NOT_EMPTY_MSG = "clientSecret must not be empty";
  private static final String USERNAME_NOT_EMPTY_MSG = "username must not be empty";
  private static final String CREDENTIALS_NOT_EMPTY_MSG = "password must not be empty";
  private static final String TOKEN_NOT_EMPTY_MSG = "token must not be empty";

  @Override
  @LogExecutionTime
  public String authorize(String tokenUrl, String clientId, String username, String password) {
    Assert.hasText(tokenUrl, TOKEN_URL_NOT_EMPTY_MSG);
    Assert.hasText(clientId, CLIENT_ID_NOT_EMPTY_MSG);
    Assert.hasText(username, USERNAME_NOT_EMPTY_MSG);
    Assert.hasText(password, CREDENTIALS_NOT_EMPTY_MSG);

    try {
      return RequestBearerTokenTask.getAccessTokenFromUserCredentials(tokenUrl, clientId,
        username, password);
    } catch (Exception ex) {
      log.error("Error occurred while obtaining AX token: ", ex);
      throw new NoSuchElementException("Can not get the access token from AX Services");
    }

  }

  @Override
  @LogExecutionTime
  public String refreshToken(String tokenUrl, String username, String password,
      String clientId, String clientSecret) {

    Assert.hasText(tokenUrl, TOKEN_URL_NOT_EMPTY_MSG);
    Assert.hasText(username, USERNAME_NOT_EMPTY_MSG);
    Assert.hasText(password, CREDENTIALS_NOT_EMPTY_MSG);
    Assert.hasText(clientId, CLIENT_ID_NOT_EMPTY_MSG);
    Assert.hasText(clientSecret, CLIENT_SECRET_NOT_EMPTY_MSG);
    try {
      String validRefreshToken =
          RequestRefreshTokenTask.getAccessAndRefreshTokenFromUserCredentials(tokenUrl,
              clientId, username, password);

      // Refresh token
      return RefreshTokenTask.refreshToken(validRefreshToken, clientId, clientSecret);
    } catch (Exception ex) {
      log.error("Error occurred while obtaining AX token: ", ex);
      return authorize(tokenUrl, clientId, username, password);
    }
  }

  @Override
  public Optional<AuthorizationFailure> tokenStatus(String tokenUrl, String token) {
    Assert.hasText(tokenUrl, TOKEN_URL_NOT_EMPTY_MSG);
    Assert.hasText(token, TOKEN_NOT_EMPTY_MSG);
    return ValidateTokenTask.validate(token, tokenUrl);
  }

}
