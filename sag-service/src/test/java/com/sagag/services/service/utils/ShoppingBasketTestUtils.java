package com.sagag.services.service.utils;

import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.domain.sag.erp.PriceWithArticle;
import com.sagag.services.domain.sag.erp.PriceWithArticlePrice;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;

import lombok.experimental.UtilityClass;

import java.util.Arrays;

/**
 * Utilities for Shopping basket test cases.
 */
@UtilityClass
public final class ShoppingBasketTestUtils {

  public static ShoppingCart createSampleShoppingCartHasDepot(boolean pfandMode) {
    final ShoppingCart cart = new ShoppingCart();
    final ShoppingCartItem item =
        new ShoppingCartItem(ArticleDocDtoDataTestProvider.createArticleDoc(), 2, 1);
    item.setCartKey("cart_key");

    final ShoppingCartItem depotCartItem = new ShoppingCartItem(createPfandArticleDoc(), 2, 1);
    depotCartItem.setDepot(true);
    depotCartItem.setPfand(pfandMode);
    item.setAttachedCartItems(Arrays.asList(depotCartItem));
    cart.setItems(Arrays.asList(item));
    return cart;
  }

  private static ArticleDocDto createPfandArticleDoc() {
    final ArticleDocDto article = new ArticleDocDto();
    PriceWithArticlePrice priceWithArticlePrice = new PriceWithArticlePrice();
    priceWithArticlePrice.setUvpePrice(3.0);
    article.setIdSagsys("1234567");
    article.setArtnr("test");
    PriceWithArticle priceWithArticle = new PriceWithArticle();
    priceWithArticle.setPrice(priceWithArticlePrice);
    article.setPrice(priceWithArticle);
    return article;
  }

  public static Address createSampleAddress() {
    final Address address = new Address();
    address.setAddressId("123456789");
    address.setCity("City");
    address.setCompanyName("Company Name");
    address.setCountry("Country");
    address.setPrimary(true);
    return address;
  }

  public static ExternalOrderRequest createSampleOrderRequest() {
    return new ExternalOrderRequest();
  }

}
