package com.sagag.services.service.api;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.offer.OfferDto;
import com.sagag.services.service.request.dms.DmsExportRequest;

/**
 * Interface to define the services for DMS.
 */
public interface DmsService {

  /**
   * Exports the offer.
   *
   * @param user the user info
   * @param data the export data
   */
  void export(final UserInfo user, final DmsExportRequest data);

  /**
   * Downloads dms export result.
   *
   * @param userId the logged in user id
   * @param customerNr the customer number
   * @return string content of exported result
   */
  String download(Long userId, String customerNr);

  /**
   * Adds offer to shopping cart.
   *
   * @param user the logged in user
   * @param offer the offer to add to shopping cart
   */
  void addOfferToShoppingCart(UserInfo user, OfferDto offer, ShopType shopType);

}
