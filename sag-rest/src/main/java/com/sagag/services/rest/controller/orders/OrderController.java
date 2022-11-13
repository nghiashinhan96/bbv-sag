package com.sagag.services.rest.controller.orders;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.domain.sag.erp.OrderConfirmation;
import com.sagag.services.rest.authorization.annotation.CanUsedSubShoppingCartPreAuthorization;
import com.sagag.services.rest.swagger.docs.ApiDesc;
import com.sagag.services.service.api.OrderBusinessService;
import com.sagag.services.service.cart.support.ShopTypeDefault;
import com.sagag.services.service.exception.OrderException;
import com.sagag.services.service.order.model.ApiRequestType;
import com.sagag.services.service.request.order.CreateOrderRequestBodyV2;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller class for Orders.
 */
@RestController
@RequestMapping("/order/v2")
@Api(tags = "Ordering APIs")
@CanUsedSubShoppingCartPreAuthorization
public class OrderController {

  private final static Boolean NOT_IMPACT_KSO_SPLIT = null;

  @Autowired
  private OrderBusinessService orderService;


  /**
   * Creates the order to ERP system.
   *
   * @param authed the authenticated user request
   * @param body the request body to create order
   * @return the {@link List<OrderConfirmation>}
   * @throws OrderException
   */
  @ApiOperation(value = ApiDesc.Order.CREATE_ORDERS_DESC, notes = ApiDesc.Order.CREATE_ORDERS_NOTE)
  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public List<OrderConfirmation> createOrder(final HttpServletRequest request,
      final OAuth2Authentication authed, @ShopTypeDefault ShopType shopType,
      @RequestBody CreateOrderRequestBodyV2 body,
      @RequestParam("ksoDisabled") boolean ksoDisabled)
      throws ServiceException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return orderService.createOrder(user, body, ApiRequestType.ORDER, shopType, ksoDisabled);
  }


  /**
   * Creates the basket to ERP system.
   *
   * @param authed the authenticated user request
   * @param body the request body to create order
   * @return the {@link List<OrderConfirmation>}
   * @throws OrderException
   */
  @ApiOperation(value = ApiDesc.Order.CREATE_BASKET_DESC, notes = ApiDesc.Order.CREATE_BASKET_NOTE)
  @PostMapping(value = "/create/basket", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public List<OrderConfirmation> createBasket(final HttpServletRequest request,
      final OAuth2Authentication authed, @ShopTypeDefault ShopType shopType,
      @RequestBody CreateOrderRequestBodyV2 body) throws ServiceException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return orderService.createOrder(user, body, ApiRequestType.TRANSFER_BASKET, shopType, NOT_IMPACT_KSO_SPLIT);
  }

  /**
   * Creates the offer to ERP system.
   *
   * @param authed the authenticated user request
   * @param body the request body to create offer
   * @return the {@link List<OrderConfirmation>}
   * @throws OrderException
   */
  @ApiOperation(value = ApiDesc.Order.CREATE_OFFERS_DESC, notes = ApiDesc.Order.CREATE_OFFERS_NOTE)
  @PostMapping(value = "/create/offer", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public List<OrderConfirmation> createOffer(final HttpServletRequest request,
      final OAuth2Authentication authed, @ShopTypeDefault ShopType shopType,
      @RequestBody CreateOrderRequestBodyV2 body) throws ServiceException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return orderService.createOrder(user, body, ApiRequestType.OFFER, shopType, NOT_IMPACT_KSO_SPLIT);
  }

  /**
   * Update final customer order when leave the sub basket.
   *
   * @param request
   * @param authed the authenticated user request
   * @param body the request body
   * @throws ServiceException
   */
  @ApiOperation(value = ApiDesc.Order.UPDATE_FINAL_CUSTOMER_ORDERS_DESC,
      notes = ApiDesc.Order.UPDATE_FINAL_CUSTOMER_ORDERS_NOTE)
  @PostMapping(value = "/final-customer/update")
  @ResponseStatus(value = HttpStatus.OK)
  public void updateFinalCustomerOrder(final HttpServletRequest request,
      final OAuth2Authentication authed, @RequestBody CreateOrderRequestBodyV2 body)
      throws ServiceException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    orderService.updateOrder(user, body);
  }
}
