package com.sagag.services.tools.support;

import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;
import com.microsoft.aad.adal4j.ClientCredential;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RefreshTokenTask extends TaskBase {

  public static String refreshToken(String refreshToken, String clientID, String clientSecret)
      throws InterruptedException, ExecutionException, MalformedURLException {
    Future<AuthenticationResult> newAuthResult;
    service = Executors.newFixedThreadPool(1);

    AuthenticationContext context = new AuthenticationContext(AUTHORITY, false, service);
    newAuthResult =
        context.acquireTokenByRefreshToken(refreshToken, new ClientCredential(clientID,
            clientSecret), null);

    return newAuthResult.get().getAccessToken();
  }
}
