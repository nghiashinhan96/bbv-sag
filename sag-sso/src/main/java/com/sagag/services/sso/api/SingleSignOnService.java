package com.sagag.services.sso.api;

import java.util.Optional;

import com.sagag.services.sso.tasks.AuthorizationFailure;

/**
 * <p>
 * The interface to authorize e-Connect with others SAG services.
 * </p>
 *
 */
public interface SingleSignOnService {

  /**
   * Authorizes ERP token.
   */
  String authorize(String tokenUrl, String clientId, String username,
      String password);

  /**
   * Refreshes ERP token.
   */
  String refreshToken(String tokenUrl, String username,
      String password, String clientId, String clientSecret);

  /**
   * Verifies token status
   */
  Optional<AuthorizationFailure> tokenStatus(String tokenUrl, String token);

}
