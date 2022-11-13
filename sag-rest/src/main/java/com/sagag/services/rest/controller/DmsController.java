package com.sagag.services.rest.controller;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.api.OfferService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.offer.OfferDto;
import com.sagag.services.common.enums.PermissionEnum;
import com.sagag.services.hazelcast.api.ActiveDmsUserCacheService;
import com.sagag.services.hazelcast.api.HaynesProCacheService;
import com.sagag.services.hazelcast.domain.cart.CustomShoppingCart;
import com.sagag.services.rest.authorization.annotation.CanUsedSubShoppingCartPreAuthorization;
import com.sagag.services.rest.authorization.annotation.IsAccessibleUrlPreAuthorization;
import com.sagag.services.rest.resource.DefaultResource;
import com.sagag.services.service.api.CartBusinessService;
import com.sagag.services.service.api.DmsService;
import com.sagag.services.service.api.UserBusinessService;
import com.sagag.services.service.cart.support.ShopTypeDefault;
import com.sagag.services.service.request.dms.DmsExportRequest;
import com.sagag.services.service.virtualuser.VirtualUserHelper;
import io.swagger.annotations.Api;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class to define service APIs for DMS.
 */
@RestController
@RequestMapping("/dms")
@Api(tags = "DMS APIs")
@IsAccessibleUrlPreAuthorization
public class DmsController {

  @Autowired
  private DmsService dmsService;

  @Autowired
  private CartBusinessService cartBusinessService;

  @Autowired
  private OfferService offerService;

  @Autowired
  private ActiveDmsUserCacheService activeDmsUserCacheService;

  @Autowired
  private UserBusinessService userBusinessService;

  @Autowired
  private HaynesProCacheService haynesProSearchService;

  @Autowired
  private VirtualUserHelper virtualUserHelper;

  /**
   * Builds DMS data for export file.
   *
   * @param authed the authenticated user
   * @param requestData the order export data
   */
  @PostMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public void exportOffer(HttpServletRequest request, final OAuth2Authentication authed,
      @RequestBody DmsExportRequest requestData) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    dmsService.export(user, requestData);
  }

  /**
   * Downloads HTML string.
   *
   * @param request
   * @param authed
   * @return the content file
   */
  @GetMapping(value = "/download", produces = MediaType.TEXT_PLAIN_VALUE)
  public String download(HttpServletRequest request, final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    final long userId = user.getId();
    userBusinessService.clearCacheUser(userId);
    haynesProSearchService.clearLabourTimes(user.key());
    activeDmsUserCacheService.remove(userId);
    virtualUserHelper.releaseDataForVirtualUser(user);
    return dmsService.download(userId, user.getCustNrStr());
  }

  /**
   * Add multiple articles to shopping basket by the list of article numbers.
   *
   * @param authed the authenticated user.
   * @param request
   *
   * @return {@link CustomShoppingCart}
   *
   */
  @PostMapping(value = "/cart/articles/add", produces = MediaType.APPLICATION_JSON_VALUE)
  @CanUsedSubShoppingCartPreAuthorization
  public CustomShoppingCart addArticlesToShoppingCart(HttpServletRequest request,
      final OAuth2Authentication authed, @RequestParam("articleNrs") final String[] articleNrs,
      @RequestParam("articleQuantities") final Integer[] quantities,
      @ShopTypeDefault ShopType shopType) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return cartBusinessService.addMultipleArticlesByArticleNumbers(user, articleNrs, quantities,
        shopType);
  }

  /**
   * Adds all vendor articles and haynespro provider work to shopping basket by the offerId.
   *
   * @param authed the authenticated user.
   * @param offerId the id of offer
   *
   * @return {@link DefaultResource}
   *
   */
  @PostMapping(value = "/cart/offer/{offerId}/add", produces = MediaType.APPLICATION_JSON_VALUE)
  @CanUsedSubShoppingCartPreAuthorization
  public DefaultResource addOfferToShoppingCart(HttpServletRequest request,
      final OAuth2Authentication authed, @PathVariable("offerId") final Long offerId,
      @ShopTypeDefault ShopType shopType) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    if (user.getPermissions().stream()
        .noneMatch(p -> p.getPermission().equals(PermissionEnum.OFFER.name()))) {
      // This api requires not only dms but also offer permission
      throw new AccessDeniedException("The function requires offer permission");
    }
    final OfferDto offer = offerService.getOfferDetails(user, offerId)
        .orElseThrow(() -> new ValidationException("Not found offer details info"));
    dmsService.addOfferToShoppingCart(user, offer, shopType);
    return DefaultResource.ok("Add offer successfully");
  }

}
