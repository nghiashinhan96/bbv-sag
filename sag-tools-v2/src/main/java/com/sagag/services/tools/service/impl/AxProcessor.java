package com.sagag.services.tools.service.impl;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClientResponseException;

import com.sagag.services.tools.exception.AxExternalException;
import com.sagag.services.tools.service.SingleSignOnService;
import com.sagag.services.tools.service.ax.config.AxInitialResource;
import com.sagag.services.tools.support.AuthorizationFailure;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AxProcessor {

  @Value("${external.webservice.ax.tokenPath}")
  private String tokenPath;

  @Value("${external.webservice.ax.clientId}")
  private String clientId;

  @Value("${external.webservice.ax.clientSecret}")
  private String clientSecret;

  @Value("${external.webservice.ax.userName}")
  private String userName;

  @Value("${external.webservice.ax.password}")
  private String password;

  @Autowired
  private AxInitialResource axInitialResource;

  @Autowired
  private SingleSignOnService ssoService;

  /**
   * Returns the current ax token to communicate with AX ERP,
   * if token is expired it's maintained by refresh token.
   */
  protected String getAxToken() {
    long start = System.currentTimeMillis();
    String axAccessToken = axInitialResource.getAccessToken();
    if (ssoService.hasToken(axAccessToken)) {
      log.debug("Current AX Access token = \n{}", axAccessToken);

      Optional<AuthorizationFailure> authFailureOpt =
          ssoService.tokenStatus(tokenPath, axAccessToken);
      if (authFailureOpt.isPresent()) {
        log.debug("Current token is expired");

        // Maintain token is expired.
        axAccessToken =
            ssoService.refreshToken(tokenPath, userName, password, clientId, clientSecret);
        axInitialResource.setAccessToken(axAccessToken);

        log.debug("After refresh Ax access token : \n{}", axAccessToken);
      }
    }
    log.debug("Perf:AxServiceImpl->getAxToken {} ms",
        System.currentTimeMillis() - start);

    return axAccessToken;
  }

  protected <T> T getOrThrow(Supplier<T> getSupplier,
      Function<RestClientResponseException, ? extends AxExternalException> exception) {
    try {
      return getSupplier.get();
    } catch (RestClientResponseException ex) {
      throw exception.apply(ex);
    }
  }

  protected <T> T getOrDefaultThrow(Supplier<T> getSupplier) {
    return getOrThrow(getSupplier, AxExternalException::new);
  }

}
