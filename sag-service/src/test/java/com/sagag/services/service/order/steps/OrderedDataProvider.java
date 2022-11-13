package com.sagag.services.service.order.steps;

import com.sagag.eshop.repo.entity.SupportedAffiliate;
import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class OrderedDataProvider {

  private static final String VIN_1 = "zfa19800004361472";

  private static final String VIN_2 = "VF1KZ1Y0248342975";

  public ExternalOrderRequest initExternalOrderRequest() {
    ExternalOrderRequest request = new ExternalOrderRequest();
    request.setMessage("message");
    request.setCustomerRefText("customerRefText");
    return request;
  }

  public List<ShoppingCartItem> initCartItems() {
    final List<ShoppingCartItem> cartItems = new ArrayList<>();
    cartItems.add(buildGlassBodyWorkCartItem(false, VIN_1, "VEH_1"));
    cartItems.add(buildGlassBodyWorkCartItem(true, VIN_2, "VEH_2"));
    cartItems.add(buildGlassBodyWorkCartItem(false, VIN_2, "VEH_3"));
    cartItems.add(buildGlassBodyWorkCartItem(true, VIN_1, "VEH_4"));
    cartItems.add(buildGlassBodyWorkCartItem(false, StringUtils.EMPTY, "VEH_5"));
    cartItems.add(buildGlassBodyWorkCartItem(true, StringUtils.EMPTY, "VEH_6"));
    return cartItems;
  }

  public List<ShoppingCartItem> initCartItemsForServiceSchedulesCase() {
    final List<ShoppingCartItem> cartItems = new ArrayList<>();
    cartItems.add(buildGlassBodyWorkCartItem(true, StringUtils.EMPTY, "VEH_7"));
    cartItems.add(buildGlassBodyWorkCartItem(true, StringUtils.EMPTY, "VEH_8"));
    cartItems.add(buildGlassBodyWorkCartItem(true, StringUtils.EMPTY, StringUtils.EMPTY));
    return cartItems;
  }

  public ShoppingCartItem buildGlassBodyWorkCartItem(boolean isGlassBodyWork, String vin, String vehId) {
    ShoppingCartItem cartItem = new ShoppingCartItem();

    ArticleDocDto article = new ArticleDocDto();
    article.setGlassOrBody(isGlassBodyWork);
    cartItem.setArticle(article);

    VehicleDto vehicle = new VehicleDto();
    vehicle.setVin(vin);
    if (!StringUtils.isBlank(vehId)) {
      vehicle.setVehId("vehIdwith" + vehId);
    }
    cartItem.setVehicle(vehicle);
    return cartItem;
  }

  public SupportedAffiliate initSupportedAffWorkingTime() {
    SupportedAffiliate affiliate = new SupportedAffiliate();
    affiliate.setStartWorkingTime("05:00");
    affiliate.setEndWorkingTime("17:00");
    return affiliate;
  }

}
