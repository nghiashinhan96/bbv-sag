package com.sagag.services.rest.controller.integration;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.rest.swagger.docs.ApiDesc;
import com.sagag.services.service.api.CartBusinessService;
import com.sagag.services.service.cart.support.ShopTypeDefault;
import com.sagag.services.thule.config.ThuleProfile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/thule", produces = MediaType.APPLICATION_JSON_VALUE)
@ThuleProfile
@Api(tags = "Thule Integration APIs")
public class ThuleController {

  @Autowired
  private CartBusinessService cartBusService;

  @ApiOperation(
      value = ApiDesc.Thule.TRANSFER_BASKET_DESC,
      notes = ApiDesc.Thule.TRANSFER_BASKET_NOTE)
  @PostMapping("/add-buyers-guide")
  @ResponseStatus(HttpStatus.OK)
  public ShoppingCart addBuyersGuide(OAuth2Authentication authed,
      @RequestBody final Map<String, String> thuleFormData, @ShopTypeDefault ShopType shopType) {
    return cartBusService.addBuyersGuideCartFromThule(UserInfo.class.cast(authed.getPrincipal()),
        thuleFormData, shopType);
  }
}
