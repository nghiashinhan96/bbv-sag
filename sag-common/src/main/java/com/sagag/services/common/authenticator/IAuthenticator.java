package com.sagag.services.common.authenticator;

public interface IAuthenticator<T, R> {

  /**
   * Authorizes the session info.
   *
   * @param session the object of session request
   * @param args the additional args
   * @return the result of object
   */
  R authenticate(T session, String... args);
}
