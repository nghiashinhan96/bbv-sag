package com.sagag.services.tools.service;


import com.sagag.services.tools.support.AuthorizationFailure;

import java.util.Optional;

/**
 * <p>
 * The interface to authorize e-Connect with others SAG services.
 * </p>
 *
 */
public interface SingleSignOnService {

  String authorize(String tokenUrl, String clientId, String username, String password);

  boolean hasToken(String token);

  Optional<AuthorizationFailure> tokenStatus(String tokenUrl, String token);

  String refreshToken(String tokenUrl, String username,
      String password, String clientId, String clientSecret);

}
