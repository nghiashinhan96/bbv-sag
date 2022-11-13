package com.sagag.services.oauth2.processor;

import com.sagag.services.oauth2.model.user.EshopUserDetails;

public interface UserAuthenticationProcessor {

  /**
   * Returns Eshop user details.
   *
   * @param details
   * @return the updated <code>EshopUserDetails</code>
   */
  EshopUserDetails process(EshopUserDetails details, Object... args);

}
