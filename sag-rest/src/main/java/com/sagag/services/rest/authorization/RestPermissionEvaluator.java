package com.sagag.services.rest.authorization;

import com.sagag.services.rest.authorization.impl.AbstractAuthorization;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * Customized rest permission evaluator at method level.
 */
@Component
@Slf4j
public class RestPermissionEvaluator implements PermissionEvaluator {

  @Autowired
  private List<AbstractAuthorization> authorizes;

  @Override
  public boolean hasPermission(Authentication authed, Object targetDomainObject,
      Object permission) {
    IAuthorize author = getAuthorization(permission.toString());
    return author.authorize(authed, targetDomainObject);
  }

  @Override
  public boolean hasPermission(Authentication authentication, Serializable targetId,
      String targetType, Object permission) {
    return hasPermission(authentication, new PermissionReference(targetId, targetType), permission);
  }

  private AbstractAuthorization getAuthorization(String type) {
    return authorizes.stream().filter(authorize -> authorize.support(type)).findFirst()
        .orElseThrow(() -> {
          final String msg = String.format("Not yet suppport %s authorization", type);
          log.warn(msg);
          return new UnsupportedOperationException(msg);
        });
  }

  @Value
  static class PermissionReference {
    private final Serializable targetId;
    private final String targetType;
  }
}
