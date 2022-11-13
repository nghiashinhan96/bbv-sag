package com.sagag.services.ax.token;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sagag.services.article.api.token.ErpAuthenService;
import com.sagag.services.ax.config.AxInitialResource;
import com.sagag.services.common.profiles.DynamicAxProfile;
import com.sagag.services.sso.api.SingleSignOnService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@DynamicAxProfile
public class TokenAuthenTypeService implements ErpAuthenService {

  //Refresh access token per 50 minutes.
  private static final long REFRESH_TOKEN_FIXED_RATE = 50 * 60 * 1000l;

  private static final long RETRY_REFRESH_TOKEN_INTERVALS_FIXED_RATE = 3 * 1000l;

  private static final int MAX_RETRY_TIMES = 10;

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

  @Override
  @Scheduled(fixedRate = REFRESH_TOKEN_FIXED_RATE)
  public void executeTask() {
    log.debug("Refresh token after current once is expired");
    String accessToken = StringUtils.EMPTY;
    try {
      accessToken = ssoService.refreshToken(tokenPath,
          userName,
          password,
          clientId,
          clientSecret);

      // Maintain token is expired.
      axInitialResource.setAccessToken(accessToken);
      axInitialResource.setNeedRetry(false);
    } catch (UnsupportedOperationException ex) {
      axInitialResource.setNeedRetry(true);
      throw ex;
    }
  }

  @Override
  @Scheduled(fixedRate = RETRY_REFRESH_TOKEN_INTERVALS_FIXED_RATE)
  public void retryRefreshErpAccessToken() {
    if (!axInitialResource.isNeedRetry()) {
      return;
    }
    log.debug("Retrying refresh ERP access token");
    for (int time = 1; time <= MAX_RETRY_TIMES; time++) {
      log.debug("We have retried refresh at {} times", time);
      executeTask();
      if (!axInitialResource.isNeedRetry()) {
        log.debug("We get the new token at {} time", time);
        break;
      }
    }
  }

  @Override
  public String getAxToken() {

    return axInitialResource.getAccessToken();
  }

  /**
   * Returns the current ax token to communicate with AX ERP,
   * if token is expired it's maintained by refresh token.
   */
  @Override
  public String refreshAccessToken() {
    executeTask();

    return getAxToken();
  }

}
