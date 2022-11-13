package com.sagag.services.service.api.impl;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.api.LicenseService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.domain.eshop.dto.LicenseSettingsDto;
import com.sagag.services.hazelcast.api.CartManagerService;
import com.sagag.services.hazelcast.domain.CartItemDto;
import com.sagag.services.hazelcast.domain.cart.CartKeyGenerators;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.request.ShoppingCartRequestBody;
import com.sagag.services.hazelcast.request.UpdateAmountRequestBody;
import com.sagag.services.ivds.api.IvdsArticleService;
import com.sagag.services.service.api.CartBusinessService;
import com.sagag.services.service.api.VinPackageService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class VinPackageServiceImpl implements VinPackageService {

  @Autowired
  private CartBusinessService cartBusinessService;

  @Autowired
  private CartManagerService cartManagerService;

  @Autowired
  private IvdsArticleService ivdsArticleService;

  @Autowired
  private LicenseService licenseService;

  @Override
  @Transactional(readOnly = true)
  public List<LicenseSettingsDto> searchVinPackages() {
    log.debug("Searching vin packages");
    return licenseService.getAllVinLicenses();
  }

  @Override
  @Transactional(readOnly = true)
  public int searchAvailableVinCalls(final Long custNr) {
    log.debug("Searching available vin calls by custNr = {}", custNr);
    Assert.notNull(custNr, "The given customer number must not be null");
    return licenseService.getVinCallsLeft(custNr);
  }

  @Override
  public ShoppingCart addVinItemToShoppingCart(final UserInfo user,
      final LicenseSettingsDto license) {
    log.debug("Adding vin item to shopping cart by license = {}", license);
    final String custNr = user.getCustNrStr();
    Assert.hasText(custNr, "The given customer should not be null");
    Assert.notNull(license, "The given license must not be null");

    final String vinArticleId = String.valueOf(license.getPackArticleId());

    // check if there's existing a VIN package in cache
    final String cartKey = CartKeyGenerators.cartKey(user.key(), SagConstants.KEY_NO_VEHICLE,
        vinArticleId, ShopType.DEFAULT_SHOPPING_CART);

    final Optional<CartItemDto> cachedCartItem = cartManagerService.findByKey(user.key(), cartKey);
    if (cachedCartItem.isPresent()) {
      log.debug("Vin package is existing in the shopping basket");
      final int updatedQuantity = cachedCartItem.get().getQuantity() + 1;
      final UpdateAmountRequestBody body = new UpdateAmountRequestBody();
      body.setPimId(vinArticleId);
      body.setVehicleId(SagConstants.KEY_NO_VEHICLE);
      body.setAmount(updatedQuantity);
      return cartBusinessService.update(user, body, ShopType.DEFAULT_SHOPPING_CART);
    }
    final ShoppingCartRequestBody vinCartRequest = new ShoppingCartRequestBody();
    vinCartRequest.setArticle(ivdsArticleService.searchVinArticle(user,
        String.valueOf(license.getPackArticleId())));
    vinCartRequest.setLicense(license);
    vinCartRequest.setQuantity(NumberUtils.INTEGER_ONE);
    return cartBusinessService.add(user, Arrays.asList(vinCartRequest),
        ShopType.DEFAULT_SHOPPING_CART);
  }

}
