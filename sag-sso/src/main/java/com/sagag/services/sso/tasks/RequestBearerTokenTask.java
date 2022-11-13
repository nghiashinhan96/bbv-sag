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
    log.debug("getAccessTokenFromUserCredentials => "
        + "\nappUrl={}\nclientId={}\nuserName={}\npass={}", appUrl, clientId, userName, password);
    try {
      service = Executors.newFixedThreadPool(1);
      log.debug("Creating the authentication context with authority url = {}", AUTHORITY);
      AuthenticationContext context = new AuthenticationContext(AUTHORITY, false, service);
      log.debug("Acquiring the token");
      Future<AuthenticationResult> future =
          context.acquireToken(appUrl, clientId, userName, password, null);
      result = future.get();
      if (result == null) {
        log.warn("Service unavailable, we can not get new access token");
        throw new ServiceUnavailableException("authentication result was null");
      }
    } catch (Exception ex) {
      log.error("Get access token has error: {}", ex);
      throw ex;
    } finally {
      service.shutdown();
    }
    return result.getAccessToken();
  }
}
