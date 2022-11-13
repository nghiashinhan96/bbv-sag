package com.sagag.services.tools.support;

import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.naming.ServiceUnavailableException;

public class RequestRefreshTokenTask extends TaskBase {

  public static String getAccessAndRefreshTokenFromUserCredentials(String app_url,
      String client_id, String username, String password) throws ServiceUnavailableException,
      InterruptedException, ExecutionException, MalformedURLException {

    try {
      service = Executors.newFixedThreadPool(1);
      AuthenticationContext context = new AuthenticationContext(AUTHORITY, false, service);
      /*
       * Acquiring access token either with user/password or client secret.
       */
      Future<AuthenticationResult> future =
          context.acquireToken(app_url, client_id, username, password, null);
      result = future.get();

    } finally {
      service.shutdown();
    }

    if (result == null) {
      throw new ServiceUnavailableException("authentication result was null");
    }
    return result.getRefreshToken();
  }
}
