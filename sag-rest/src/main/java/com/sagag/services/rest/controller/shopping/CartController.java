package com.sagag.services.rest.controller.shopping;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.exporter.ExportStreamedResult;
import com.sagag.services.common.exporter.SupportedExportType;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.hazelcast.api.CartManagerService;
import com.sagag.services.hazelcast.api.ContextService;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.domain.user.EshopContext;
import com.sagag.services.hazelcast.request.ShoppingCartRequestBody;
import com.sagag.services.hazelcast.request.UpdateAmountRequestBody;
import com.sagag.services.rest.authorization.annotation.CanUsedSubShoppingCartPreAuthorization;
import com.sagag.services.rest.swagger.docs.ApiDesc;
import com.sagag.services.service.api.CartBusinessService;
import com.sagag.services.service.cart.support.ShopTypeDefault;
import com.sagag.services.service.exception.ExportException;
import com.sagag.services.service.exporter.BasketExporter;
import com.sagag.services.service.exporter.shoppingcart.ShoppingCartCsvExporter;
import com.sagag.services.service.exporter.shoppingcart.ShoppingCartExcelExporter;
import com.sagag.services.service.exporter.shoppingcart.ShoppingCartExportItemDto;
import com.sagag.services.service.exporter.shoppingcart.ShoppingCartWordExporter;
import com.sagag.services.service.exporter.shoppingcart.ShortShoppingCartCsvExporter;
import com.sagag.services.service.exporter.shoppingcart.ShortShoppingCartExcelExporter;
import com.sagag.services.service.exporter.shoppingcart.ShortShoppingCartExportItemDto;
import com.sagag.services.service.request.RemoveItemRequestBody;
import com.sagag.services.service.request.UpdateDisplayedPriceRequestItem;
import com.sagag.services.service.request.order.OrderHistoryDetailRequestBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to define APIs for shopping cart.
 */
@RestController
@RequestMapping("/cart")
@Api(tags = "Shopping Cart APIs")
public class CartController {

  @Autowired
  private CartBusinessService cartBusinessService;

  @Autowired
  private CartManagerService cartManagerService;

  @Autowired
  private ShoppingCartCsvExporter shoppingCartCsvExporter;

  @Autowired
  private ShoppingCartExcelExporter shoppingCartExcelExporter;

  @Autowired
  private ShoppingCartWordExporter shoppingCartWordExporter;

  @Autowired
  private ShortShoppingCartCsvExporter shortShoppingCartCsvExporter;

  @Autowired
  private ShortShoppingCartExcelExporter shortShoppingCartExcelExporter;

  @Autowired
  private BasketExporter basketExporter;

  @Autowired
  private ContextService contextService;

  /**
   * Adds an item to the shopping cart.
   *
   * @param authed the authenticated user
   * @param cartRequest the adding request body
   * @return the updated {@link ShoppingCart}.
   */
  // @formatter:off
  @ApiOperation(
      value = ApiDesc.Cart.ADD_ARTICLE_TO_SHOPPING_CART_API_DESC,
      notes = ApiDesc.Cart.ADD_ARTICLE_TO_SHOPPING_CART_NOTE
      )
  @PostMapping(
      value = "/article/add",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
      )
  // @formatter:on
  @CanUsedSubShoppingCartPreAuthorization
  public ShoppingCart addToShoppingCart(final HttpServletRequest request,
      final OAuth2Authentication authed, @ShopTypeDefault ShopType shopType,
      @RequestBody final ShoppingCartRequestBody cartRequest) {
    if (!cartRequest.hasArticle()) {
      throw new IllegalArgumentException("the article should not be null.");
    }
    cartRequest.setOverideExisting(true);
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return cartBusinessService.add(user, Arrays.asList(cartRequest), shopType);
  }

  /**
   * Updates the quantity for the added item in the shopping basket.
   *
   * @param authed the authenticated user
   * @param body the request body
   * @return the updated {@link ShoppingCart}
   */
  //@formatter:off
  @ApiOperation(
      value = ApiDesc.Cart.UPDATE_ARTICLE_TO_SHOPPING_CART_API_DESC,
      notes = ApiDesc.Cart.UPDATE_ARTICLE_TO_SHOPPING_CART_NOTE
      )
  @PostMapping(
      value = "/article/update",
      produces = MediaType.APPLICATION_JSON_VALUE
      )
  //@formatter:on
  @CanUsedSubShoppingCartPreAuthorization
  public ShoppingCart updateShoppingCart(final HttpServletRequest request,
      final OAuth2Authentication authed, @ShopTypeDefault ShopType shopType,
      @RequestBody final UpdateAmountRequestBody body) {
    final String pimId = body.getPimId();
    Assert.hasText(pimId, "Article Pim ID should not be null or blank.");
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return cartBusinessService.update(user, body, shopType);
  }

  /**
   * Removes the articles within vehicle in context if any.
   *
   * @param authed the authenticated user
   * @param body the request body
   * @return the updated {@link ShoppingCart}
   */
  //@formatter:off
  @ApiOperation(
      value = ApiDesc.Cart.REMOVE_ARTICLES_FROM_SHOPPING_CART_API_DESC,
      notes = ApiDesc.Cart.REMOVE_ARTICLES_FROM_SHOPPING_CART_NOTE
      )
  @PostMapping(
      value = "/article/remove",
      produces = MediaType.APPLICATION_JSON_VALUE
      )
  //@formatter:on
  @CanUsedSubShoppingCartPreAuthorization
  public ShoppingCart removeShoppingCart(final HttpServletRequest request,
      final OAuth2Authentication authed, @ShopTypeDefault ShopType shopType,
      @RequestBody final RemoveItemRequestBody body) {
    return cartBusinessService.remove((UserInfo) authed.getPrincipal(), body, shopType);
  }

  /**
   * Removes all the articles in the shopping basket.
   *
   * @param authed the authenticated user
   * @return the empty {@link ShoppingCart}
   */
  //@formatter:off
  @ApiOperation(
      value = ApiDesc.Cart.REMOVE_ALL_ARTICLES_FROM_SHOPPING_CART_API_DESC,
      notes = ApiDesc.Cart.REMOVE_ALL_ARTICLES_FROM_SHOPPING_CART_NOTE
      )
  @PostMapping(
      value = "/article/remove/all",
      produces = MediaType.APPLICATION_JSON_VALUE
      )
  //@formatter:on
  @CanUsedSubShoppingCartPreAuthorization
  public ShoppingCart removeAllShoppingCart(final HttpServletRequest request,
      final OAuth2Authentication authed, @ShopTypeDefault ShopType shopType) {
    cartBusinessService.clear((UserInfo) authed.getPrincipal(), shopType);
    return new ShoppingCart();
  }

  /**
   * Views the {@link ShoppingCart}.
   *
   * @param authed the authenticated user
   * @return the updated {@link ShoppingCart} instance.
   */
  //@formatter:off
  @ApiOperation(
      value = ApiDesc.Cart.VIEW_ARTICLE_TO_SHOPPING_CART_API_DESC,
      notes = ApiDesc.Cart.VIEW_ARTICLE_TO_SHOPPING_CART_NOTE
      )
  @GetMapping(
      value = "/view",
      produces = MediaType.APPLICATION_JSON_VALUE)
  //@formatter:on
  @CanUsedSubShoppingCartPreAuthorization
  public ShoppingCart viewShoppingCart(final HttpServletRequest request,
      final OAuth2Authentication authed, @ShopTypeDefault ShopType shopType) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return cartBusinessService.checkoutShopCart(user, shopType, true);
  }

  /**
   * Views the shopping cart in cache first to improve UX performance the viewShoppingCart run
   * follow up as async to update avail and price if any from slow ERP API.
   *
   * @return the updated {@link ShoppingCart} instance.
   */
  //@formatter:off
  @ApiOperation(
      value = ApiDesc.Cart.VIEW_ARTICLE_TO_SHOPPING_CART_API_DESC,
      notes = ApiDesc.Cart.VIEW_ARTICLE_TO_SHOPPING_CART_NOTE
      )
  @PostMapping(
      value = "/view/cache",
      produces = MediaType.APPLICATION_JSON_VALUE
      )
  //@formatter:on
  @CanUsedSubShoppingCartPreAuthorization
  public ShoppingCart checkoutCachedShoppingCart(final HttpServletRequest request,
      final OAuth2Authentication authed, @ShopTypeDefault ShopType shopType) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return cartBusinessService.checkoutShopCart(user, shopType, true, false);
  }

  /**
   * Views quickly the {@link ShoppingCart}.
   *
   * @param authed the authenticated user.
   * @return the updated {@link ShoppingCart} instance.
   */
  //@formatter:off
  @ApiOperation(
      value = ApiDesc.Cart.QUICK_VIEW_ARTICLE_TO_SHOPPING_CART_API_DESC,
      notes = ApiDesc.Cart.QUICK_VIEW_ARTICLE_TO_SHOPPING_CART_NOTE
      )
  @GetMapping(
      value = "/quickview",
      produces = MediaType.APPLICATION_JSON_VALUE
      )
  //@formatter:on
  @CanUsedSubShoppingCartPreAuthorization
  public ShoppingCart quickViewShoppingCart(final HttpServletRequest request,
      final OAuth2Authentication authed, @ShopTypeDefault ShopType shopType) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    // reload cart without availability request
    final boolean isAvailabilityReq = user.isSaleOnBehalf();
    return cartBusinessService.checkoutShopCart(user, shopType, isAvailabilityReq);
  }

  //@formatter:off
  @GetMapping(
      value = "/address",
      produces = MediaType.APPLICATION_JSON_VALUE
      )
  //@formatter:on
  public List<Address> getAdress(final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return user.getAddresses();
  }

  @PostMapping(value = "/exportcsv")
  @ResponseBody
  public ResponseEntity<byte[]> exportCsvShoppingBasket(
      @RequestBody List<ShoppingCartExportItemDto> data) throws ServiceException {
    return shoppingCartCsvExporter.exportCsv(data).buildResponseEntity();
  }

  @PostMapping(value = "/export-short-csv")
  @ResponseBody
  public ResponseEntity<byte[]> exportCsvShortShoppingBasket(
      @RequestBody List<ShortShoppingCartExportItemDto> data) throws ServiceException {
    return shortShoppingCartCsvExporter.exportCsv(data).buildResponseEntity();
  }

  @PostMapping(value = "/exportexcel")
  @ResponseBody
  public ResponseEntity<byte[]> exportExcelShoppingBasket(
      @RequestBody List<ShoppingCartExportItemDto> data) throws ServiceException {
    return shoppingCartExcelExporter.exportExcel(data).buildResponseEntity();
  }

  @PostMapping(value = "/export-short-excel")
  @ResponseBody
  public ResponseEntity<byte[]> exportExcelShortShoppingBasket(
      @RequestBody List<ShortShoppingCartExportItemDto> data,
      @RequestParam(value = "sheetName", required = false) String sheetName)
      throws ServiceException {
    return shortShoppingCartExcelExporter.exportExcel(data, sheetName).buildResponseEntity();
  }

  @PostMapping(value = "/exportword")
  @ResponseBody
  public ResponseEntity<byte[]> exportWordShoppingBasket(
      @RequestBody List<ShoppingCartExportItemDto> data) throws ServiceException {
    return shoppingCartWordExporter.exportWord(data).buildResponseEntity();
  }

  @PostMapping(value = "/export-basket", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<byte[]> exportRftShoppingBasket(final OAuth2Authentication authed,
      @RequestParam(value = "curentStateNetPriceView") Boolean curentStateNetPriceView,
      @RequestParam(value = "formatType") SupportedExportType formatType) throws ExportException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    final EshopContext eshopContext = contextService.getEshopContext(user.key());

    final ExportStreamedResult result =
        basketExporter.export(user, eshopContext, curentStateNetPriceView, formatType);
    return result.buildResponseEntity();
  }

  /**
   * Adds order history by orderId to shopping basket.
   *
   * @param authed the authenticated user
   * @param body the request body
   * @return {@link ShoppingCart} instance
   */
  @PostMapping(
      value = { "/order-history/add", "/article/addOrderHistory" },
      produces = MediaType.APPLICATION_JSON_VALUE
      )
  @ApiOperation(
      value = ApiDesc.Cart.ADD_ORDERS_TO_BASKET_DESC,
      notes = ApiDesc.Cart.ADD_ORDERS_TO_BASKET_NOTE
      )
  @CanUsedSubShoppingCartPreAuthorization
  public ShoppingCart addOrderHistoryToCart(final HttpServletRequest request,
      final OAuth2Authentication authed, @ShopTypeDefault ShopType shopType,
      @RequestBody OrderHistoryDetailRequestBody body) {
    final UserInfo user = (UserInfo) authed.getPrincipal();

    return cartBusinessService.addOrderToCart(user, body.getOrderId(), body.getOrderNumber(),
        shopType, body.getBasketItemSourceId(), body.getBasketItemSourceDesc());
  }

  /**
   * Adds all articles of saved basket histories to the current shopping cart by basketId.
   *
   * @param basketId the id of basket history to add.
   * @param authed the authenticated user.
   * @return the updated {@link ShoppingCart}.
   */
  @ApiOperation(value = ApiDesc.Cart.ADD_SAVED_BASKET_TO_SHOPPING_CART_API_DESC)
  @PostMapping(
      value = { "/basket-history/add/{basketId}", "/article/addBasketHistory/{basketId}" },
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
      )
  @CanUsedSubShoppingCartPreAuthorization
  public ShoppingCart addSavedBasketToShoppingCart(final HttpServletRequest request,
      final OAuth2Authentication authed, @PathVariable final Long basketId,
      @ShopTypeDefault ShopType shopType) {
    final UserInfo user = (UserInfo) authed.getPrincipal();

    return cartBusinessService.addSavedBasketToShoppingCart(user, basketId, shopType);
  }

  /**
   * Get total quantity of all articles in {@link ShoppingCart}.
   *
   * @return the updated {@link ShoppingCart} instance.
   */
  //@formatter:off
  @ApiOperation(
      value = ApiDesc.Cart.QUICK_VIEW_ARTICLE_TO_SHOPPING_CART_API_DESC,
      notes = ApiDesc.Cart.QUICK_VIEW_ARTICLE_TO_SHOPPING_CART_NOTE
      )
  @GetMapping(
      value = "/total",
      produces = MediaType.APPLICATION_JSON_VALUE
      )
  //@formatter:on
  public int quickViewTotal(@RequestParam(value = "userid") String userid,
      @RequestParam(value = "custNr") String custNr, @ShopTypeDefault ShopType shopType) {
    return cartManagerService.getLatestTotalQuantity(UserInfo.userKey(userid, custNr), shopType);
  }

  /**
   * Returns the updated shopping basket with newest price info from ERP.
   *
   * @param authed the authenticated user
   * @return the updated {@link ShoppingCart}.
   */
  @ApiOperation(value = ApiDesc.Cart.UPDATE_PRICES_SHOPPING_CART)
  @GetMapping(value = "/update/prices", produces = MediaType.APPLICATION_JSON_VALUE)
  @CanUsedSubShoppingCartPreAuthorization
  public ShoppingCart updatePricesShoppingCart(final HttpServletRequest request,
      final OAuth2Authentication authed, @ShopTypeDefault ShopType shopType) {
    return cartBusinessService.updatePricesForShoppingCart((UserInfo) authed.getPrincipal(),
        shopType);
  }

  /**
   * Counts the total number of order positions in the shopping basket.
   *
   * @param authed the authenticated user who requests
   * @return the total number of order positions
   */
  @ApiOperation(value = ApiDesc.Cart.COUNT_TOTAL_ITEMS_IN_SHOPPING_CART_API_DESC,
      notes = ApiDesc.Cart.COUNT_TOTAL_ITEMS_IN_SHOPPING_CART_NOTE)
  @GetMapping(value = "/position/count", produces = MediaType.APPLICATION_JSON_VALUE)
  @CanUsedSubShoppingCartPreAuthorization
  public int countOrderPositions(final HttpServletRequest request,
      final OAuth2Authentication authed, @ShopTypeDefault ShopType shopType) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return cartBusinessService.countOrderPositions(user.getCachedUserId(), user.getCustNrStr(),
        shopType);
  }


  /**
   * Updates displayed price from shopping basket.
   *
   * @param authed the authenticated user
   * @param body the request body
   * @return
   */
  @ApiOperation(value = ApiDesc.Cart.UPDATE_DISPLAYED_PRICE_API_DESC,
      notes = ApiDesc.Cart.UPDATE_DISPLAYED_PRICE_API_NOTE)
  @PostMapping(value = "/displayed-price/update", produces = MediaType.APPLICATION_JSON_VALUE)
  @CanUsedSubShoppingCartPreAuthorization
  public ShoppingCart updateDisplayedPrices(final HttpServletRequest request,
      final OAuth2Authentication authed, @ShopTypeDefault ShopType shopType,
      @RequestBody final List<UpdateDisplayedPriceRequestItem> items) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return cartBusinessService.updateDisplayedPrices(user, items, shopType);
  }

}
