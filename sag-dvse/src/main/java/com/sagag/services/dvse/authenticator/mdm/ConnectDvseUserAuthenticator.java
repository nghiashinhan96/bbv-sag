package com.sagag.services.dvse.authenticator.mdm;

import org.springframework.stereotype.Component;

import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.dvse.dto.dvse.ConnectUser;
import com.sagag.services.dvse.profiles.DefaultDvseProfile;
import com.sagag.services.dvse.wsdl.dvse.User;

/**
 * Implementation class for authenticate DVSE user.
 */
@Component
@DefaultDvseProfile
public class ConnectDvseUserAuthenticator extends AbstractDvseUserAuthenticator
  implements IMdmUserAuthenticator<User> {

  /**
   * <p>
   * Authenticate MDM user to e-Connect and get customer info from ERP.
   * </p>
   *
   * @param mdmUser the object of {@link User}
   * @return the object of {@link ConnectUser}
   *
   */
  @LogExecutionTime
  @Override
  public ConnectUser authenticate(final User mdmUser, final String... externalSessionIds) {
    return findConnectUser(mdmUser.getCustomerId(), mdmUser.getUsername(),
        mdmUser.getPassword(), externalSessionIds[0]);
  }
}
