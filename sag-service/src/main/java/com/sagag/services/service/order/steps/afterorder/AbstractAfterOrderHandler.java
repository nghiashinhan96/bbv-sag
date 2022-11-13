package com.sagag.services.service.order.steps.afterorder;

import com.sagag.eshop.service.api.LicenseService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.hazelcast.api.VinOrderCacheService;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.service.order.steps.AfterOrderHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public abstract class AbstractAfterOrderHandler implements AfterOrderHandler {

  @Value("${license.vin.minimumOrder:}")
  private String minimumOrder;

  @Autowired
  private LicenseService licenseService;

  @Autowired
  private VinOrderCacheService vinOrderCacheService;

  /**
   * Handle vin search when order successfully. Redeem license table and clear cache if shopping
   * basket price matches condition
   *
   * @param user the logged in user
   * @param shoppingCart shopping cart item
   */
  protected void handleVinSearchCount(UserInfo user, ShoppingCart shoppingCart) {
    final String key = user.key();
    Integer searchCount = vinOrderCacheService.getSearchCount(key);
    if (searchCount == null) {
      return;
    }
    // redeem
    if (shoppingCart.getSubTotalWithNet() >= Double.valueOf(minimumOrder)) {
      licenseService.redeemVinLicense(user.getCustNrStr(), searchCount);
    }
    // clear cache
    vinOrderCacheService.clearSearchCount(key);
  }

}
