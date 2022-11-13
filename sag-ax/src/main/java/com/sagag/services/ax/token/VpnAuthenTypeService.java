package com.sagag.services.ax.token;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.sagag.services.article.api.token.ErpAuthenService;
import com.sagag.services.common.profiles.WintProfile;

import lombok.extern.slf4j.Slf4j;

@Service
@WintProfile
@Slf4j
public class VpnAuthenTypeService implements ErpAuthenService {

  @Override
  public String getAxToken() {
    return StringUtils.EMPTY;
  }

  @Override
  public void executeTask() {
    log.debug("No need token for vpn erp type");
  }

  @Override
  public void retryRefreshErpAccessToken() {
    log.debug("No need to retry token for vpn erp type");
  }

  @Override
  public String refreshAccessToken() {
    executeTask();
    return getAxToken();
  }

}
