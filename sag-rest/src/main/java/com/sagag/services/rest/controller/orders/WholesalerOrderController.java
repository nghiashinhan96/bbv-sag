package com.sagag.services.rest.controller.orders;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.order.OrderDashboardDto;
import com.sagag.services.rest.swagger.docs.ApiDesc;
import com.sagag.services.service.api.FinalOrderBusinessService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for Wholesaler Orders.
 */
@RestController
@RequestMapping("/order/wholesaler")
@Api(tags = "Wholesaler Order Management APIs")
public class WholesalerOrderController {

  @Autowired
  private FinalOrderBusinessService finalOrderBusinessService;

  /**
   * Gets order dashboard overview for wholesaler user only.
   *
   * @param authed the current authorization
   * @return instance of {@link OrderDashboardDto}
   */
  @ApiOperation(
      value = ApiDesc.Order.GET_DASHBOARD_OVERVIEW_DESC,
      notes = ApiDesc.Order.GET_DASHBOARD_OVERVIEW_NOTE
      )
  @GetMapping(value = "/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
  public OrderDashboardDto getDashBoardOverview(final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return finalOrderBusinessService.getOrderDashBoardOverview(user);
  }

}
