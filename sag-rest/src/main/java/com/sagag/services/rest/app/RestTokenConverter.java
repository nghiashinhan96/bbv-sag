package com.sagag.services.rest.app;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.service.api.UserBusinessService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Backend JWT token converter to user object.
 */
public class RestTokenConverter extends DefaultUserAuthenticationConverter {

  private static final String SALES_ID_KEY = "sales_onbehalf";

  private static final String UNIQUE_USERNAME_KEY = "user_name";

  private static final String CLIENT_ID_KEY = "client_id";

  private static final String LOCATED_AFFILIATE_KEY = "located_affiliate";

  @Autowired
  private UserBusinessService userBusinessService;

  @Override
  @LogExecutionTime
  public Authentication extractAuthentication(final Map<String, ?> map) {
    final Authentication authed = super.extractAuthentication(map);
    if (Objects.isNull(authed)) {
      return null;
    }
    final Object salesId = map.get(SALES_ID_KEY);
    String idStr = (String) map.get(UNIQUE_USERNAME_KEY);
    if (StringUtils.containsAny(idStr, SagConstants.UNDERSCORE)) {
      idStr = StringUtils.split(idStr, SagConstants.UNDERSCORE)[0];
    }
    final Long id = Long.valueOf(idStr);
    final String clientId = (String) map.get(CLIENT_ID_KEY);
    final Optional<Long> saleIdOpt = Optional.ofNullable(salesId).map(Object::toString)
        .map(Long::valueOf);

    final String locatedAffiliate = (String) map.get(LOCATED_AFFILIATE_KEY);
    final UserInfo userInfo = userBusinessService.findCacheUser(id, locatedAffiliate,
        clientId, saleIdOpt);
    return new UsernamePasswordAuthenticationToken(userInfo, "N/A", authed.getAuthorities());
  }

}
