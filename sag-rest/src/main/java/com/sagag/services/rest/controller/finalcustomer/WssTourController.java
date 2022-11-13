package com.sagag.services.rest.controller.finalcustomer;

import com.sagag.eshop.service.api.WssTourService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.WssTourValidationException;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.domain.eshop.dto.WssTourDto;
import com.sagag.services.domain.eshop.tour.dto.WssTourSearchRequestCriteria;
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

/**
 * The controller exposes API for WSS customer Tour.
 *
 */
@RestController
@RequestMapping("/wss/tour")
@Api(tags = "WSS Tour")
public class WssTourController {

  @Autowired
  private WssTourService wssTourService;

  @ApiOperation(value = ApiDesc.WssTour.CREATE_NEW_WSS_TOUR_DESC,
      notes = ApiDesc.WssTour.CREATE_NEW_WSS_TOUR_NOTE)
  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  @HasWholesalerPreAuthorization
  public WssTourDto create(OAuth2Authentication authed, @RequestBody final WssTourDto request)
      throws WssTourValidationException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return wssTourService.create(request, user.getOrganisationId());
  }


  @ApiOperation(value = ApiDesc.WssTour.UPDATE_WSS_TOUR_DESC,
      notes = ApiDesc.WssTour.UPDATE_WSS_TOUR_NOTE)
  @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
  @HasWholesalerPreAuthorization
  public WssTourDto update(OAuth2Authentication authed, @RequestBody final WssTourDto request)
      throws WssTourValidationException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return wssTourService.update(request, user.getOrganisationId());
  }

  @ApiOperation(value = ApiDesc.WssTour.REMOVE_WSS_TOUR_DESC,
      notes = ApiDesc.WssTour.REMOVE_WSS_TOUR_NOTE)
  @DeleteMapping(value = "/remove/{wssTourId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  @HasWholesalerPreAuthorization
  public void remove(OAuth2Authentication authed,
      @PathVariable("wssTourId") final Integer wssTourId) throws WssTourValidationException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    wssTourService.remove(wssTourId, user.getOrganisationId());
  }

  @ApiOperation(value = ApiDesc.WssTour.SEARCH_WSS_TOUR_DESC,
      notes = ApiDesc.WssTour.SEARCH_WSS_TOUR_NOTE)
  @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  @HasWholesalerPreAuthorization
  public Page<WssTourDto> searchTour(OAuth2Authentication authed,
      @RequestBody final WssTourSearchRequestCriteria criteria,
      @PageableDefault final Pageable pageable) throws ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    criteria.setOrgId(user.getOrganisationId());
    return RestExceptionUtils
        .doSafelyReturnNotEmptyRecords(wssTourService.searchTourByCriteria(criteria, pageable));
  }

  @ApiOperation(value = ApiDesc.WssTour.GET_WSS_TOUR_BY_ID_DESC,
      notes = ApiDesc.WssTour.GET_WSS_TOUR_BY_ID_NOTE)
  @GetMapping(value = "/{wssTourId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  @HasWholesalerPreAuthorization
  public WssTourDto getWssTourDetail(OAuth2Authentication authed,
      @PathVariable("wssTourId") final Integer wssTourId) throws WssTourValidationException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return wssTourService.getWssTourDetail(wssTourId, user.getOrganisationId());
  }


}
