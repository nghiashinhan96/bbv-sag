package com.sagag.services.dvse.authenticator.mdm;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.sagag.services.common.authenticator.IAuthenticator;
import com.sagag.services.dvse.dto.dvse.ConnectUser;

/**
 * Interface to provide authenticate MDM user with DVSE application.
 */
@FunctionalInterface
public interface IMdmUserAuthenticator<T> extends IAuthenticator<T, ConnectUser> {

  /**
   * Authorizes MDM user with DVSE application.
   *
   * @param mdmUser the MDM user info
   * @return the user info of DVSE application of {@link ConnectUser}
   */
  @Override
  ConnectUser authenticate(T mdmUser, String... args);

  default <R> R authenticateAndExecuteFunction(T mdmUser, String externalSessionId,
      Function<ConnectUser, R> processor) {
    final BiFunction<T, String, ConnectUser> authorizeFunc =
        (user, extSId) -> authenticate(user, extSId);
    return authorizeFunc.andThen(processor).apply(mdmUser, externalSessionId);
  }

}
