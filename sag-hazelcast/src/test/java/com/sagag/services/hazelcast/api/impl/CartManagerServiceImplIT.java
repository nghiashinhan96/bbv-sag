package com.sagag.services.hazelcast.api.impl;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.OwnSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.domain.sag.erp.PriceWithArticle;
import com.sagag.services.domain.sag.erp.PriceWithArticlePrice;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.hazelcast.api.CartManagerService;
import com.sagag.services.hazelcast.app.HazelcastApplication;
import com.sagag.services.hazelcast.domain.CartItemDto;
import com.sagag.services.hazelcast.domain.cart.CartItemDtoBuilders;
import com.sagag.services.hazelcast.request.ShoppingCartRequestBody;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Optional;

/**
 * Integration test class for cart manager.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { HazelcastApplication.class })
@EshopIntegrationTest
public class CartManagerServiceImplIT {

  @Autowired
  private CartManagerService cartManagerService;

  private static final String CART_KEY = "29_1111595_V58944M27193_1000929868";
  private static final String CUSTOMER_ID = "1111595";
  private static final Long USER_ID = 29L;
  private static final String USER_KEY = StringUtils.join(Arrays.asList(USER_ID, CUSTOMER_ID), SagConstants.UNDERSCORE);

  @After
  public void destroy() {
    cartManagerService.clearCartByUserIds(Arrays.asList(USER_ID));
  }

  @Test
  public void modifyNumberOfCartItemThenCountTheShoppingCart() {
    cartManagerService.clearCartByUserIds(Arrays.asList(USER_ID));
    int count = cartManagerService.countOrderPositions(USER_KEY, CUSTOMER_ID,
        ShopType.DEFAULT_SHOPPING_CART);
    Assert.assertThat(count, Matchers.greaterThanOrEqualTo(0));
    final CartItemDto addingItem = buildCartItemDto(CART_KEY);
    cartManagerService.add(addingItem);
    count = cartManagerService.countOrderPositions(String.valueOf(USER_ID), CUSTOMER_ID,
        ShopType.DEFAULT_SHOPPING_CART);
    Assert.assertThat(count, Matchers.greaterThanOrEqualTo(0));
    cartManagerService.remove(USER_KEY, CART_KEY);
    count = cartManagerService.countOrderPositions(String.valueOf(USER_ID), CUSTOMER_ID,
        ShopType.DEFAULT_SHOPPING_CART);
    Assert.assertThat(count, Matchers.greaterThanOrEqualTo(0));
  }

  @Test
  public void addCartItemThenClearAll() {
    cartManagerService.clearCartByUserIds(Arrays.asList(USER_ID));
    int count = cartManagerService.countOrderPositions(USER_KEY, CUSTOMER_ID,
        ShopType.DEFAULT_SHOPPING_CART);
    Assert.assertThat(count, Matchers.greaterThanOrEqualTo(0));
    String cartKey1 = "29_1111595_V58944M27193_1000929868";
    String cartKey2 = "29_1111595_V58944M27193_1000929869";
    final CartItemDto addingItem1 = buildCartItemDto(cartKey1);
    final CartItemDto addingItem2 = buildCartItemDto(cartKey2);
    cartManagerService.add(addingItem1);
    cartManagerService.add(addingItem2);
    count = cartManagerService.countOrderPositions(USER_KEY, CUSTOMER_ID,
        ShopType.DEFAULT_SHOPPING_CART);
    Assert.assertThat(count, Matchers.greaterThanOrEqualTo(0));
    cartManagerService.clearCart(String.valueOf(USER_ID), CUSTOMER_ID,
        ShopType.DEFAULT_SHOPPING_CART);
    count = cartManagerService.countOrderPositions(USER_KEY, CUSTOMER_ID,
        ShopType.DEFAULT_SHOPPING_CART);
    Assert.assertThat(count, Matchers.greaterThanOrEqualTo(0));

  }

  @Test
  public void givenCartItemDto_ShouldAddToCart() {
    // check item with this cart key if exists
    cartManagerService.remove(USER_KEY, CART_KEY);
    final Optional<CartItemDto> item = cartManagerService.findByKey(USER_KEY, CART_KEY);
    Assert.assertThat(item.isPresent(), Matchers.is(false));
    // add new item from this cart key
    final CartItemDto addingItem = buildCartItemDto(CART_KEY);
    cartManagerService.add(addingItem);
    // verify that the item was added
    final Optional<CartItemDto> addedItem = cartManagerService.findByKey(USER_KEY, CART_KEY);
    Assert.assertThat(addedItem.isPresent(), Matchers.is(true));
  }

  private static CartItemDto buildCartItemDto(final String cartKey) {

    final UserInfo user = new UserInfo();
    user.setId(29l);
    user.setUsername("sales02");
    Customer customer = new Customer();
    customer.setNr(1111595L);
    OwnSettings ownSettings = new OwnSettings();
    user.setCustomer(customer);
    user.setSettings(ownSettings);
    user.setRoles(Collections.emptyList());

    final ShoppingCartRequestBody request = new ShoppingCartRequestBody();
    request.setArticle(buildArticleDocDto());
    request.setVehicle(createVehicleDto());
    request.setQuantity(3);

    final CartItemDto cartItem = CartItemDtoBuilders.build(cartKey, user, request,
        ShopType.DEFAULT_SHOPPING_CART);
    cartItem.setAddedTime(Calendar.getInstance().getTime());
    cartItem.setItemDesc("added item desc test");

    return cartItem;
  }

  private static VehicleDto createVehicleDto() {
    return VehicleDto.builder()
        .id("V58944M27193")
        .vehId("V58944M27193")
        .ktType(58944)
        .idMake(121)
        .idModel(11221)
        .idMotor(27193)
        .vehicleName("1.4 TSI")
        .vehicleBrand("VW")
        .vehicleModel("GOLF VII Kombi (BA5)")
        .build();
  }

  private static ArticleDocDto buildArticleDocDto() {
    final ArticleDocDto article = new ArticleDocDto();
    article.setId("1000929868");
    article.setIdSource("datenpool");
    article.setIdUmsart(StringUtils.EMPTY);
    article.setArtnr("1091298");
    article.setIdSagsys("1000929868");
    article.setSupplier("QUICK");
    article.setSupplierId(0);
    article.setArtid("1000929868");
    article.setAmountNumber(1);
    article.setSalesQuantity(1);

    PriceWithArticlePrice articlePrice = new PriceWithArticlePrice();
    articlePrice.setGrossPrice(10d);
    PriceWithArticle price = new PriceWithArticle();
    price.setPrice(articlePrice);
    article.setPrice(price);
    return article;
  }
}
