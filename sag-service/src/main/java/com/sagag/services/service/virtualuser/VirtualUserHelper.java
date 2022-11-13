package com.sagag.services.service.virtualuser;

import com.google.common.collect.Lists;
import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.api.ExternalUserService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.hazelcast.api.CartManagerService;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VirtualUserHelper {

  @Autowired
  private ExternalUserService externalUserService;

  @Autowired
  private CartManagerService cartManagerService;

  /**
   * Releases Virtual User Data.
   *
   * @param user the logged-in user
   */
  public void releaseDataForVirtualUser(UserInfo user) {
    if (!user.isOciVirtualUser()) {
      return;
    }
    final Long userIdLong = user.getId();
    final String userIdStr = String.valueOf(userIdLong);
    Stream.of(ShopType.values())
      .forEach(shopType -> cartManagerService.clearCart(userIdStr, user.getCustNrStr(), shopType));
    if (user.getSupportedAffiliate() != null && user.getSupportedAffiliate().isDvseAffiliate()) {
      externalUserService.releaseVirtualUsers(Lists.newArrayList(userIdLong));
    }

  }
}
