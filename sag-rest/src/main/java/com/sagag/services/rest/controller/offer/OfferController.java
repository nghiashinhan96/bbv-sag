package com.sagag.services.rest.controller.offer;

import com.sagag.eshop.repo.criteria.offer.OfferSearchCriteria;
import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.api.OfferService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.offer.OfferDto;
import com.sagag.eshop.service.dto.offer.OfferGeneralDto;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.exporter.SupportedExportType;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.rest.authorization.annotation.CanUsedSubShoppingCartPreAuthorization;
import com.sagag.services.rest.authorization.annotation.IsAccessibleUrlPreAuthorization;
import com.sagag.services.rest.resource.offer.OfferPageResource;
import com.sagag.services.rest.resource.offer.OfferResource;
import com.sagag.services.rest.swagger.docs.ApiDesc;
import com.sagag.services.service.api.OfferBusinessService;
import com.sagag.services.service.cart.support.ShopTypeDefault;
import com.sagag.services.service.exception.OfferExportException;
import com.sagag.services.service.exporter.OfferExporter;
import com.sagag.services.service.request.offer.OfferPositionItemRequestBody;
import com.sagag.services.service.request.offer.OfferRequestBody;
import com.sagag.services.service.request.offer.OfferSearchRequestBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;

/**
 * Offer controllers.
 */
@RestController
@RequestMapping("/offer")
@Api(tags = "Offers APIs")
@IsAccessibleUrlPreAuthorization
public class OfferController {

  @Autowired
  private OfferService offerService;

  @Autowired
  private OfferBusinessService offerBusinessService;

  @Autowired
  private OfferExporter offerExporter;

  @ApiOperation(value = ApiDesc.Offer.SEARCH_OFFERS_API_DESC,
      notes = ApiDesc.Offer.SEARCH_OFFERS_NOTE)
  @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public OfferPageResource searchOffers(HttpServletRequest request, final OAuth2Authentication auth,
      @RequestBody final OfferSearchRequestBody body, @PageableDefault final Pageable pageable)
      throws ResultNotFoundException {
    final UserInfo user = (UserInfo) auth.getPrincipal();

    final OfferSearchCriteria criteria = SagBeanUtils.map(body, OfferSearchCriteria.class);
    criteria.setOrganisationId(user.getOrganisationId());
    criteria.setCollectionShortname(user.getCollectionShortname());

    final Page<OfferGeneralDto> offers = offerService.searchOffers(criteria, pageable);
    return OfferPageResource
        .of(RestExceptionUtils.doSafelyReturnNotEmptyRecords(offers, "Not found any offer"));
  }

  @ApiOperation(value = ApiDesc.Offer.REMOVE_OFFER_API_DESC,
      notes = ApiDesc.Offer.REMOVE_OFFER_NOTE)
  @PostMapping(value = "/{offerId}/remove",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public void removeOffer(HttpServletRequest request, final OAuth2Authentication auth,
      @PathVariable("offerId") final Long offerId) {
    final UserInfo user = (UserInfo) auth.getPrincipal();
    offerService.remove(user, offerId);
  }

  @ApiOperation(value = ApiDesc.Offer.CREATE_OFFER_API_DESC,
      notes = ApiDesc.Offer.CREATE_OFFER_NOTE)
  @PostMapping(value = "/create",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public OfferResource createNewOffer(HttpServletRequest request, final OAuth2Authentication auth) {
    final UserInfo user = (UserInfo) auth.getPrincipal();
    final OfferDto offerDto = offerService.create(user);
    return OfferResource.of(offerDto);
  }

  @ApiOperation(value = ApiDesc.Offer.UPDATE_OFFER_API_DESC,
      notes = ApiDesc.Offer.UPDATE_OFFER_NOTE)
  @PostMapping(value = "/update",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public OfferResource updateOffer(HttpServletRequest request, final OAuth2Authentication auth,
      @RequestParam(value = "calculated", required = false,
          defaultValue = "false") boolean calculated,
      @RequestBody final OfferRequestBody body) {
    if (calculated) {
      final OfferDto offer = body.toOffer();

      // Update total gross price of offer positions has remark type
      offer.fillRemarks();

      // Returns the result after calculated
      return OfferResource.of(offer);
    }
    final UserInfo user = (UserInfo) auth.getPrincipal();
    return OfferResource.of(offerBusinessService.updateOffer(user, body));
  }

  @ApiOperation(value = ApiDesc.Offer.VIEW_OFFER_DETAIL_API_DESC,
      notes = ApiDesc.Offer.VIEW_OFFER_DETAIL_NOTE)
  @PostMapping(value = "/{offerId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public OfferResource getOfferDetails(HttpServletRequest request, final OAuth2Authentication auth,
      @PathVariable("offerId") final Long offerId) throws ResultNotFoundException {
    final UserInfo user = (UserInfo) auth.getPrincipal();

    final OfferDto offer =
        RestExceptionUtils.doSafelyReturnOptionalRecord(offerService.getOfferDetails(user, offerId),
            String.format("Not found offer %s's info", offerId));
    return OfferResource.of(offer);
  }

  @PostMapping(value = "/{offerId}/export/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<byte[]> exportExistingOffer(HttpServletRequest request,
      final OAuth2Authentication auth, @PathVariable("offerId") final Long offerId,
      @PathVariable("type") final String exportTypeStr) throws OfferExportException {
    final UserInfo user = (UserInfo) auth.getPrincipal();
    final OfferDto offer =
        offerService.getOfferDetails(user, offerId).orElseThrow(() -> new NoSuchElementException(
            String.format("Not found offer details is %s to export csv", offerId)));

    final SupportedExportType exportType = SupportedExportType.getExportType(exportTypeStr);
    return offerExporter.export(user, offer, exportType).buildResponseEntity();
  }

  @PostMapping(value = "/current/export/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<byte[]> exportCurrentOffer(HttpServletRequest request,
      final OAuth2Authentication auth, @PathVariable("type") final String exportTypeStr,
      @RequestBody final OfferDto offer) throws OfferExportException {
    final UserInfo user = (UserInfo) auth.getPrincipal();
    final SupportedExportType exportType = SupportedExportType.getExportType(exportTypeStr);

    // Update receipt address as default address of offer person
    offer.fillReceipientAddress();

    return offerExporter.export(user, offer, exportType).buildResponseEntity();
  }

  @ApiOperation(value = ApiDesc.Offer.ORDER_OFFER_API_DESC, notes = ApiDesc.Offer.ORDER_OFFER_NOTE)
  @PostMapping(value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  @CanUsedSubShoppingCartPreAuthorization
  public void orderOffer(HttpServletRequest request, final OAuth2Authentication auth,
      @ShopTypeDefault ShopType shopType,
      @RequestBody List<OfferPositionItemRequestBody> offerPositionRequests) {
    final UserInfo user = (UserInfo) auth.getPrincipal();
    offerBusinessService.orderOffer(user, offerPositionRequests, shopType);
  }
}
