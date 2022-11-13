package com.sagag.services.rest.controller.finalcustomer;

import com.sagag.eshop.service.api.WssDeliveryProfileService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.WssDeliveryProfileValidationException;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.domain.eshop.criteria.WssDeliveryProfileSearchRequestCriteria;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileDto;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileRequestDto;
import com.sagag.services.rest.authorization.annotation.HasWholesalerPreAuthorization;
import com.sagag.services.rest.swagger.docs.ApiDesc;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wss/delivery-profile")
@Api(tags = "WSS Delivery Profile Management APIs")
public class WssDeliveryProfileController {

  @Autowired
  private WssDeliveryProfileService wssDeliveryProfileService;

  @ApiOperation(value = ApiDesc.WssDeliveryProfile.CREATE_NEW_WSS_DELIVERY_PROFILE_DESC,
      notes = ApiDesc.WssDeliveryProfile.CREATE_NEW_WSS_DELIVERY_PROFILE_NOTE)
  @PostMapping("/create")
  @ResponseStatus(HttpStatus.OK)
  @HasWholesalerPreAuthorization
  public void createWssDeliveryProfile(final OAuth2Authentication authed,
      @RequestBody final WssDeliveryProfileRequestDto profile)
      throws WssDeliveryProfileValidationException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    wssDeliveryProfileService.createWssDeliveryProfile(profile, user.getOrganisationId());
  }

  @ApiOperation(value = ApiDesc.WssDeliveryProfile.UPDATE_WSS_DELIVERY_PROFILE_DESC,
      notes = ApiDesc.WssDeliveryProfile.UPDATE_WSS_DELIVERY_PROFILE_NOTE)
  @PutMapping("/update")
  @ResponseStatus(HttpStatus.OK)
  @HasWholesalerPreAuthorization
  public void updateWssDeliveryProfile(final OAuth2Authentication authed,
      @RequestBody final WssDeliveryProfileRequestDto profile)
      throws WssDeliveryProfileValidationException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    wssDeliveryProfileService.updateWssDeliveryProfile(profile, user.getOrganisationId());
  }

  @ApiOperation(value = ApiDesc.WssDeliveryProfile.REMOVE_WSS_DELIVERY_PROFILE_DESC,
      notes = ApiDesc.WssDeliveryProfile.REMOVE_WSS_DELIVERY_PROFILE_NOTE)
  @DeleteMapping("/{wssDeliveryProfileId}/remove")
  @ResponseStatus(HttpStatus.OK)
  @HasWholesalerPreAuthorization
  public void removeWssDeliveryProfile(final OAuth2Authentication authed,
      @PathVariable final Integer wssDeliveryProfileId)
      throws WssDeliveryProfileValidationException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    wssDeliveryProfileService.removeWssDeliveryProfile(wssDeliveryProfileId,
        user.getOrganisationId());
  }

  @ApiOperation(value = ApiDesc.WssDeliveryProfile.SEARCH_WSS_DELIVERY_PROFILE_DESC,
      notes = ApiDesc.WssDeliveryProfile.SEARCH_WSS_DELIVERY_PROFILE_NOTE)
  @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  @HasWholesalerPreAuthorization
  public Page<WssDeliveryProfileDto> searchDeliveryProfile(OAuth2Authentication authed,
      @RequestBody final WssDeliveryProfileSearchRequestCriteria criteria,
      @PageableDefault final Pageable pageable) throws ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    criteria.setOrgId(user.getOrganisationId());
    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(
        wssDeliveryProfileService.searchDeliveryProfileByCriteria(criteria, pageable));
  }

  @ApiOperation(value = ApiDesc.WssDeliveryProfile.ADD_WSS_DELIVERY_PROFILE_TOUR_DESC,
      notes = ApiDesc.WssDeliveryProfile.ADD_WSS_DELIVERY_PROFILE_TOUR_NOTE)
  @PostMapping("/tour/add")
  @ResponseStatus(HttpStatus.OK)
  @HasWholesalerPreAuthorization
  public void addDeliveryProfileTour(final OAuth2Authentication authed,
      @RequestBody final WssDeliveryProfileRequestDto profile)
      throws WssDeliveryProfileValidationException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    wssDeliveryProfileService.addWssDeliveryProfileTour(profile, user.getOrganisationId());
  }

  @ApiOperation(value = ApiDesc.WssDeliveryProfile.UPDATE_WSS_DELIVERY_PROFILE_TOUR_DESC,
      notes = ApiDesc.WssDeliveryProfile.UPDATE_WSS_DELIVERY_PROFILE_TOUR_NOTE)
  @PutMapping("/tour/update")
  @ResponseStatus(HttpStatus.OK)
  @HasWholesalerPreAuthorization
  public void updateDeliveryProfileTour(final OAuth2Authentication authed,
      @RequestBody final WssDeliveryProfileRequestDto profile)
      throws WssDeliveryProfileValidationException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    wssDeliveryProfileService.updateWssDeliveryProfileTour(profile, user.getOrganisationId());
  }

  @ApiOperation(value = ApiDesc.WssDeliveryProfile.REMOVE_WSS_DELIVERY_PROFILE_TOUR_DESC,
      notes = ApiDesc.WssDeliveryProfile.REMOVE_WSS_DELIVERY_PROFILE_TOUR_NOTE)
  @DeleteMapping("/tour/{wssDeliveryProfileTourId}/remove")
  @ResponseStatus(HttpStatus.OK)
  @HasWholesalerPreAuthorization
  public void removeWssDeliveryProfileTour(final OAuth2Authentication authed,
      @PathVariable final Integer wssDeliveryProfileTourId)
      throws WssDeliveryProfileValidationException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    wssDeliveryProfileService.removeWssDeliveryProfileTour(wssDeliveryProfileTourId,
        user.getOrganisationId());
  }

  @ApiOperation(value = ApiDesc.WssDeliveryProfile.GET_WSS_DELIVERY_PROFILE_TOUR_DETAIL_DESC,
      notes = ApiDesc.WssDeliveryProfile.GET_WSS_DELIVERY_PROFILE_TOUR_DETAIL_NOTE)
  @GetMapping("/{wssDeliveryProfileId}")
  @ResponseStatus(HttpStatus.OK)
  @HasWholesalerPreAuthorization
  public WssDeliveryProfileDto getDeliveryProfileDetail(final OAuth2Authentication authed,
      @PathVariable final Integer wssDeliveryProfileId)
      throws WssDeliveryProfileValidationException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return wssDeliveryProfileService.getWssDeliveryProfileDetail(wssDeliveryProfileId,
        user.getOrganisationId());
  }
}
