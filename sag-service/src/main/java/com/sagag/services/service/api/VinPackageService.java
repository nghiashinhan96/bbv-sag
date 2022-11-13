package com.sagag.services.service.api;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.eshop.dto.LicenseSettingsDto;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;

import java.util.List;

public interface VinPackageService {

  /**
   * Returns all available VIN licenses.
   *
   * @return a list of VIN license settings
   */
  List<LicenseSettingsDto> searchVinPackages();

  /**
   * Returns the available VIN calls.
   *
   * @param customerNr the customer number
   * @return the result of VIN calls
   */
  int searchAvailableVinCalls(Long customerNr);

  /**
   * Adds VIN item into shopping cart of user login.
   *
   * @param user the current user
   * @param license the license request object
   * @return the result of {@link ShoppingCart}
   */
  ShoppingCart addVinItemToShoppingCart(UserInfo user, LicenseSettingsDto license);

}
