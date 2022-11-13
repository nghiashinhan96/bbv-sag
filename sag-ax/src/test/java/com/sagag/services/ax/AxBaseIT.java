package com.sagag.services.ax;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sagag.services.sso.api.SingleSignOnService;
import com.sagag.services.sso.tasks.AuthorizationFailure;

import lombok.extern.slf4j.Slf4j;

/**
 * Base class to build some common method for testing.
 *
 */
@Slf4j
public class AxBaseIT {

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
  private SingleSignOnService ssoService;

  public String getAxToken(String cachedToken) {
    long start = System.currentTimeMillis();
    String cachedAccessToken = null;
    if (!StringUtils.isBlank(cachedToken)) {
      log.debug("Current AX Access token = \n{}", cachedToken);

      Optional<AuthorizationFailure> optional =
          ssoService.tokenStatus(tokenPath, cachedToken);
      if (optional.isPresent()) {
        log.debug("Current token is expired");

        cachedAccessToken =
            ssoService.refreshToken(tokenPath, userName, password, clientId, clientSecret);

        log.debug("After refresh Ax access token : \n{}", cachedAccessToken);
      } else {
        cachedAccessToken = cachedToken;
      }
    } else {
      cachedAccessToken = ssoService.authorize(tokenPath, clientId, userName, password);
      log.debug("Get new Ax access token : \n{}", cachedAccessToken);
    }
    log.debug("Perf:AxServiceImpl->getAxToken {} ms",
        System.currentTimeMillis() - start);

    return cachedAccessToken;
  }

  public void assertOkHttpStatus(ResponseEntity<?> res) {
    log.debug("Result object: {}", res.getBody());
    Assert.assertThat(res.getStatusCode(), Matchers.equalTo(HttpStatus.OK));
  }

  public void assertSuccessfulHttpStatus(ResponseEntity<?> res) {
    log.debug("Result object: {}", res.getBody());
    Assert.assertThat(res.getStatusCode().is2xxSuccessful(), Matchers.equalTo(true));
  }
}
