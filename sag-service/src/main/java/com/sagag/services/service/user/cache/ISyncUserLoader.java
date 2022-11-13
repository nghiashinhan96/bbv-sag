package com.sagag.services.service.user.cache;

import com.sagag.eshop.service.dto.UserInfo;

import java.util.Optional;

public interface ISyncUserLoader {

  /**
   * Synchronizes the user session details on server side and cache it to avoid performance.
   * <p>
   * This method should be run whenever there was a change on user settings or from ERP. The cache
   * should be cleared when user log-outs.
   *
   * @param currentLoggedUserId the current logged user id
   * @param loginAffiliate
   * @param clientId
   * @param saleIdOpt
   * @return the user details in cache
   */
  UserInfo load(long userId, String loginAffiliate, String clientId, Optional<Long> saleIdOpt);

}
