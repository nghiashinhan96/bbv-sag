package com.sagag.services.rest.controller.shopping;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.BasketHistoryDto;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.rest.authorization.annotation.CanUsedSubShoppingCartPreAuthorization;
import com.sagag.services.rest.resource.BasketHistoryResource;
import com.sagag.services.service.api.BasketService;
import com.sagag.services.service.cart.support.ShopTypeDefault;
import com.sagag.services.service.request.BasketHistoryRequestBody;
import com.sagag.services.service.request.BasketHistorySearchRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller to define APIs for saved basket processing.
 */
@RestController
@RequestMapping("/basket")
@Api(tags = "Saved Basket APIs")
public class BasketController {

  private static final String GET_BASKET_HISTORY_DETAILS_API_DESC =
      "The service to get the detail of basket history by basket id";

  private static final String DELETE_BASKET_HISTORY_API_DESC =
      "The service to delete the history by basket id";

  @Autowired
  private BasketService basketService;

  /**
   * Search all history from the criteria.
   *
   * @param authed the user who requests
   * @param request the search criteria
   * @return the basket history resource - {@link BasketHistoryResource}
   */
  // @formatter:off
  @ApiOperation(
      value = "The service to search the list of basket histories of user login or sales user by criteria"
      )
  @PostMapping(
      value = "/histories",
      produces = MediaType.APPLICATION_JSON_VALUE
      )
  public BasketHistoryResource search(
      final OAuth2Authentication authed,
      @RequestBody final BasketHistorySearchRequest request) {
    // @formatter:on
    BasketHistoryResource resource = new BasketHistoryResource();
    resource.setBasketHistories(
        basketService.getBasketHistories(request, (UserInfo) authed.getPrincipal()));
    resource.setTotalItems(resource.getBasketHistories().getTotalElements());
    resource.add(linkTo(methodOn(BasketController.class).search(authed, request)).withSelfRel());

    return resource;
  }

  /**
   * Creates the basket history.
   *
   * @param authed the authenticated user
   * @param body the request body
   */
  //@formatter:off
  @ApiOperation(
      value = "The service to create new basket history item by user login or sale user")
  @PostMapping(
      value = "/history/create",
      produces = MediaType.APPLICATION_JSON_VALUE
      )
  @ResponseStatus(HttpStatus.OK)
  @CanUsedSubShoppingCartPreAuthorization
  public void createBasketHistory(final HttpServletRequest request,
      final OAuth2Authentication authed,
      @ShopTypeDefault ShopType shopType,
      @RequestBody final BasketHistoryRequestBody body) {
    // @formatter:on
    basketService.createBasketHistory(body, (UserInfo) authed.getPrincipal(), shopType);
  }

  // @formatter:off
  @ApiOperation(
      value = "The service to count the total of basket histories of user login or sales user")
  @GetMapping(
      value = "/histories/count",
      produces = MediaType.APPLICATION_JSON_VALUE
      )
  public BasketHistoryResource countBasketHistoriesByUser(
      final OAuth2Authentication authed) {
    // @formatter:on
    final BasketHistoryResource resource = new BasketHistoryResource();
    final UserInfo user = (UserInfo) authed.getPrincipal();
    Long salesId = null;
    if (user.isSalesUser()) {
      salesId = user.getId();
    } else if (user.isSaleOnBehalf()) {
      salesId = user.getSalesId();
    }
    resource.setTotalItems(
        basketService.countBasketHistoriesByUser(user.getCustNrStr(), salesId, user.getOriginalUserId()));
    resource.add(linkTo(methodOn(BasketController.class).countBasketHistoriesByUser(authed))
        .withSelfRel());
    return resource;
  }

  /**
   * Gets details of basket history by basketId.
   *
   * @param auth the authenticated user that adds.
   * @param basketId the id of basket history to add.
   * @return the result of {@link BasketHistoryDto}
   */
  @ApiOperation(value = GET_BASKET_HISTORY_DETAILS_API_DESC)
  @GetMapping(value = "/{basketId}/history",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public BasketHistoryDto getBasketHistoryDetails(final OAuth2Authentication auth,
      @PathVariable final Long basketId) throws ResultNotFoundException {
    return basketService.getBasketHistoryDetails(basketId)
        .orElseThrow(() -> new ResultNotFoundException(
            String.format("Not found basket history with id = %d.", basketId)));
  }

  /**
   * Deletes the basket history.
   *
   * @param auth the authenticated user
   */
  @ApiOperation(value = DELETE_BASKET_HISTORY_API_DESC)
  @PostMapping(value = "/{basketId}/history/delete",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public void deleteBasket(final OAuth2Authentication auth,
      @PathVariable("basketId") final Long basketId) {
    final UserInfo user = (UserInfo) auth.getPrincipal();
    basketService.delete(user, basketId);
  }

}
