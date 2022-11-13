package com.sagag.services.service.order.handler;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.profiles.SbProfile;
import com.sagag.services.domain.eshop.dto.GrantedBranchDto;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.domain.sag.erp.LocationAvailabilityItem;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.order.model.ApiRequestType;
import com.sagag.services.service.order.model.KsoMode;
import com.sagag.services.service.order.model.LocationTypeFilter;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SbProfile
@Component
public class SbCartTypeHander extends CartTypeHandler {

  @Override
  public ApiRequestType apiRequestType() {
    return ApiRequestType.ORDER;
  }

  @Override
  public Map<LocationTypeFilter, ShoppingCart> handleShoppingCart(ShoppingCart shoppingCart,
      UserInfo user, boolean ksoDisabled) {
    Assert.notNull(shoppingCart, "Shopping cart is empty");
    Map<String, List<ShoppingCartItem>> itemsByLocation = new HashMap<>();
    shoppingCart.getItems().forEach(item -> {
      Assert.notNull(item.getArticle(), "Shopping cart item have to contain article");
      Assert.notEmpty(item.getArticle().getAvailabilities(), "Shopping cart must not include non-avail item");
      Optional<Availability> availOpt =
          item.getArticle().getAvailabilities().stream().findFirst();
      if (availOpt.isPresent()) {
        Assert.notNull(availOpt.get().getLocation(),
            "Shopping cart cannot contain not-availaible item in any location");
        availOpt.get().getLocation().getItems().forEach(location -> {
          Availability avail = availOpt.get();
          itemsByLocation.computeIfAbsent(location.getLocationId(), k -> new ArrayList<>())
              .add(createShoppingCartItemForLocation(item, location, avail));
        });
      }
    });
    Map<LocationTypeFilter, ShoppingCart> result = new HashMap<>();
    if (MapUtils.isNotEmpty(itemsByLocation)) {
      itemsByLocation.entrySet().forEach(item -> {
        result.put(
            LocationTypeFilter.builder()
                .location(GrantedBranchDto.builder().branchId(item.getKey()).build()).build(),
            new ShoppingCart(item.getValue()));
      });
    }
    return result;
  }

  private ShoppingCartItem createShoppingCartItemForLocation(ShoppingCartItem item,
      LocationAvailabilityItem location, Availability updatedAvail) {
    int updatedQuantiy = location.getQuantity().intValue();
    updatedAvail.setQuantity(updatedQuantiy);
    item.getArticle().setAvailabilities(Arrays.asList(updatedAvail));
    item.getArticle().setAmountNumber(updatedQuantiy);
    ShoppingCartItem cloneItem = SerializationUtils.clone(item);
    cloneItem.setQuantity(updatedQuantiy);
    return cloneItem;
  }

  @Override
  public KsoMode withKsoMode() {
    return KsoMode.NOT_EFFECT;
  }

}
