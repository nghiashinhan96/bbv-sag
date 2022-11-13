package com.sagag.services.service.user.handler;

import com.sagag.services.domain.sag.external.ExternalUserSession;
import com.sagag.services.hazelcast.api.UserCacheService;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Slf4j
public class ExternalUserSessionContextHandler {

  @Autowired
  private UserCacheService userCacheService;

  public void saveExternalUserSessionIntoUserContext(Long userId,
      ExternalUserSession autonetSession) {
    log.debug("Saving the autonet session into user cache by user_id = {} - session = {}",
        userId, autonetSession);
    Assert.notNull(userId, "The given user id must not be null");
    Assert.notNull(autonetSession, "The given autonet session must not be null");
    Optional.ofNullable(userCacheService.get(userId))
    .ifPresent(userInfo -> {
      userInfo.setExternalUserSession(autonetSession);
      userCacheService.put(userInfo);
    });

  }

}
