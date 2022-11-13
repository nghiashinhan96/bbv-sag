package com.sagag.services.sso.tasks;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.naming.ServiceUnavailableException;

import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestRefreshTokenTask extends TaskBase {

  public static String getAccessAndRefreshTokenFromUserCredentials(String appUrl,
      String clientId, String username, String password) throws ServiceUnavailableException,
      InterruptedException, ExecutionException, MalformedURLException {
    log.debug("getAccessAndRefreshTokenFromUserCredentials => "
        + "\nappUrl={}\nclientId={}\nusername={}\npass={}", appUrl, clientId, username, password);
    try {
      service = Executors.newFixedThreadPool(1);
      log.debug("Creating the authentication context with authority url = {}", AUTHORITY);
      AuthenticationContext context = new AuthenticationContext(AUTHORITY, false, service);
      /*
       * Acquiring access token either with user/password or client secret.
       */
      log.debug("Acquiring the token");
      Future<AuthenticationResult> future =
          context.acquireToken(appUrl, clientId, username, password, null);
      result = future.get();

    } finally {
      service.shutdown();
    }

    if (result == null) {
      log.warn("Service unavailable, we can not refresh token");
      throw new ServiceUnavailableException("authentication result was null");
    }
    return result.getRefreshToken();
  }
}
