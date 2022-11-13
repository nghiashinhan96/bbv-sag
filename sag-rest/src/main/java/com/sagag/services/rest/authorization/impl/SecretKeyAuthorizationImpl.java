package com.sagag.services.rest.authorization.impl;

import com.sagag.services.common.contants.SagConstants;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@Slf4j
public class SecretKeyAuthorizationImpl extends AbstractAuthorization {

  private static final String REFRESH_CACHE_CLIENT_ID = "eshop-web";

  private static final String REFRESH_CACHE_SECRET_KEY = "eshop-web-yztAhGpFW";

  @Override
  protected boolean hasPermission(Authentication authed, Object targetDomainObject) {
    log.debug("Input secret key = {}", targetDomainObject);
    final String hashPass = (String) targetDomainObject;
    if (StringUtils.isBlank(hashPass)) {
      return false;
    }
    final String[] credentials = decodeSecretKey().apply(hashPass);
    return ArrayUtils.getLength(credentials) == 2
        && StringUtils.equalsIgnoreCase(REFRESH_CACHE_CLIENT_ID, credentials[0])
        && StringUtils.equalsIgnoreCase(REFRESH_CACHE_SECRET_KEY, credentials[1]);
  }

  private static Function<String, String[]> decodeSecretKey() {
    return key -> StringUtils.split(new String(Base64.decodeBase64(key)), SagConstants.COLON);
  }

  @Override
  public boolean authorize(Authentication authed, Object targetDomainObject) {
    return isRoleAnonymous(authed) && hasPermission(authed, targetDomainObject);
  }

  private static boolean isRoleAnonymous(Authentication authed) {
    return authed.getAuthorities().stream()
        .allMatch(auth -> "ROLE_ANONYMOUS".equalsIgnoreCase(auth.getAuthority()));
  }

  @Override
  public String authorizeType() {
    return "isSecretKey";
  }
}
