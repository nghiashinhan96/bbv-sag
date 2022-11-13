package com.sagag.services.service.api;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.hazelcast.domain.cart.CustomShoppingCart;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.request.ShoppingCartRequestBody;
import com.sagag.services.hazelcast.request.UpdateAmountRequestBody;
import com.sagag.services.service.request.RemoveItemRequestBody;
import com.sagag.services.service.request.UpdateDisplayedPriceRequestItem;

import java.util.List;
import java.util.Map;

/**
 * Interface to define services on Shopping Cart.
 */
public interface CartBusinessService {

  /**
   * Adds an item to shopping cart.
   *
   * @param userInfo the current user who adds the item.
   * @param cartRequests the list of cart item requests.
   * @return the updated {@link ShoppingCart}
   */
  ShoppingCart add(UserInfo userInfo, List<ShoppingCartRequestBody> cartRequests,
      ShopType shopType);

  /**
   * Updates the quantity for added item.
   *
   * @param user the current user who adds the item.
   * @param vehicleId the vehicle id in context
   * @param artNumber the article number
   * @param quantity the new quantity to update. <code>quantity = 0</code> means to delete.
   * @return the updated {@link ShoppingCart}
   */
  ShoppingCart update(UserInfo user, UpdateAmountRequestBody body, ShopType shopType);

  /**
   * Removes all articles within the vehicle in context.
   *
   * @param user the current user who views the shopping basket.
   * @param body the RemoveItemRequestBody
   * @return the updated {@link ShoppingCart}
   */
  ShoppingCart remove(UserInfo user, RemoveItemRequestBody body, ShopType shopType);

  /**
   * Clears all shopping basket items in the cache.
   *
   * @param user the user who requests
   */
  void clear(UserInfo user, ShopType shopType);

  /**
   * Adds order history to shopping cart.
   *
   * @param user the user login info
   * @param orderId the order id of order history
   * @param orderNumber the order orderNumber of order history
   *
   * @return the shopping basket.
   */
  ShoppingCart addOrderToCart(UserInfo user, Long orderId, String orderNumber, ShopType shopType,
                              String basketItemSourceId, String basketItemSourceDesc);

  /**
   * Adds invoices positions to cart.
   *
   * @param user the user login info
   * @param invoiceNr the invoice number
   * @param orderNumber the order number
   * @param orderId the order id of order history
   * @return the shopping basket.
   */
  ShoppingCart addInvoiceToCart(UserInfo user, String invoiceNr, String orderNr, Long orderId,
      ShopType shopType, String basketItemSourceId, String basketItemSourceDesc);

  /**
   * <p>
   * The service to add all articles of saved basket histories to the current shopping cart.
   * </p>
   *
   * <pre>
   * Support add all articles of saved basket histories for sales and sales on behalf
   * </pre>
   *
   * @param user userDetails the user login info
   * @param basketId the id of basket history to add.
   * @return the updated {@link ShoppingCart}.
   */
  ShoppingCart addSavedBasketToShoppingCart(UserInfo user, Long basketId, ShopType shopType);

  /**
   * Returns the updated shopping basket with newest price info from ERP.
   *
   * @param user the current user info
   * @return the result of {@link ShoppingCart}
   */
  ShoppingCart updatePricesForShoppingCart(UserInfo user, ShopType shopType);

  /**
   * Search articles by numbers and add to shoppingbasket.
   *
   * @param user
   * @param articleNrs
   * @param quantities
   *
   * @return {@link CustomShoppingCart}
   *
   */
  CustomShoppingCart addMultipleArticlesByArticleNumbers(UserInfo user, String[] articleNrs,
      Integer[] quantities, ShopType shopType);

  /**
   * Returns the number of order positions in the shopping cart for specific user.
   *
   * @param userId the user id who requests
   * @param customerNr the ERP customer number for that user
   * @return the number of order positions
   */
  int countOrderPositions(String userId, String custNr, ShopType shopType);

  /**
   * Adds the final customer order id to shopping cart.
   *
   * @param user the current user info
   * @param finalCustOrderId the selected final customer order id
   * @return the updated {@link ShoppingCart}.
   */
  ShoppingCart addFinalCustomerOrderToShoppingCart(UserInfo user, Long finalCustOrderId,
      ShopType shopType, String basketItemSourceId, String basketItemSourceDesc);

  /**
   * Views the shopping baskets with items and prices.
   *
   * @param user the current user who views the shopping basket.
   * @param isAvailabilityReq true if it needs to get availability
   * @return the shopping basket.
   */
  ShoppingCart checkoutShopCart(UserInfo user, ShopType shopType, boolean cacheMode,
      boolean quickViewMode);

  /**
   * Views the shopping baskets with items and prices.
   *
   * @param user the current user who views the shopping basket.
   * @param isAvailabilityReq true if it needs to get availability
   * @return the shopping basket.
   */
  ShoppingCart checkoutShopCart(UserInfo user, ShopType shopType, boolean isAvailabilityReq);

  /**
   * Adds Buyers Guide Cart Data from Thule into shopping cart.
   *
   * @param user the current user login
   * @param buyersGuideFormData the request form data
   * @param shopType the requested shop type
   */
  ShoppingCart addBuyersGuideCartFromThule(UserInfo user, Map<String, String> buyersGuideFormData,
      ShopType shopType);

  /**
   * Updates displayed price for shopping basket.
   *
   * @param user the current user login
   * @param items the body request
   * @param shopType the requested shop type
   * @return the shopping basket.
   */
  ShoppingCart updateDisplayedPrices(UserInfo user, List<UpdateDisplayedPriceRequestItem> items, ShopType shopType);
}
