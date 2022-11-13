package com.sagag.services.tools.support;

import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.naming.ServiceUnavailableException;

public class RequestBearerTokenTask extends TaskBase {

  /**
   * @param appUrl app_url
   * @param clientId client_id
   * @param userName userName
   * @param password password
   * @return accessToken
   * @throws ServiceUnavailableException ServiceUnavailableException
   * @throws InterruptedException InterruptedException
   * @throws ExecutionException ExecutionException
   * @throws MalformedURLException MalformedURLException
   */
  public static String getAccessTokenFromUserCredentials(String appUrl, String clientId,
      String userName, String password) throws ServiceUnavailableException, InterruptedException,
      ExecutionException, MalformedURLException {
    try {
      service = Executors.newFixedThreadPool(1);
      AuthenticationContext context = new AuthenticationContext(AUTHORITY, false, service);
      Future<AuthenticationResult> future =
          context.acquireToken(appUrl, clientId, userName, password, null);
      result = future.get();
      if (result == null) {
        throw new ServiceUnavailableException("authentication result was null");
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      service.shutdown();
    }
    return result.getAccessToken();
  }
}
