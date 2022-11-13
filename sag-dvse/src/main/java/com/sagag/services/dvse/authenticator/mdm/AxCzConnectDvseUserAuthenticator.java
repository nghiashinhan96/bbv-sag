package com.sagag.services.dvse.authenticator.mdm;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.sagag.services.dvse.dto.dvse.ConnectUser;
import com.sagag.services.dvse.profiles.AxCzDvseProfile;
import com.sagag.services.dvse.wsdl.tmconnect.Credentials;
import com.sagag.services.dvse.wsdl.tmconnect.User;

@Component
@AxCzDvseProfile
public class AxCzConnectDvseUserAuthenticator extends AbstractDvseUserAuthenticator
  implements IMdmUserAuthenticator<Credentials> {

  @Override
  public ConnectUser authenticate(Credentials mdmUser, String... externalSessionIds) {
    Assert.notNull(mdmUser, "The given authorization user must not be null");
    Assert.notNull(mdmUser.getCatalogUserCredentials(), "Not found credentails in request");

    final User user = mdmUser.getCatalogUserCredentials();
    final String customerId = user.getCustomerId();
    final String username = user.getUsername();
    final String password = user.getPassword();
    final String extSessionId = externalSessionIds[0];

    return findConnectUser(customerId, username, password, extSessionId);
  }

}
