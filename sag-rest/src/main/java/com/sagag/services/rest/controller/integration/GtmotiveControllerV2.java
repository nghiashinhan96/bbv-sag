package com.sagag.services.rest.controller.integration;

import com.sagag.eshop.service.api.VinErrorLogService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.domain.eshop.dto.VinErrorLogDto;
import com.sagag.services.gtmotive.config.GtmotiveProfile;
import com.sagag.services.gtmotive.dto.request.gtinterface.*;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotivePartsListSearchRequest;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotiveReferenceSearchByPartCodesCriteria;
import com.sagag.services.gtmotive.dto.response.gtinterface.GtmotiveMultiPartSearchResponse;
import com.sagag.services.gtmotive.dto.response.gtinterface.GtmotiveVinSecurityCheckResponse;
import com.sagag.services.gtmotive.exception.GtmotiveXmlResponseProcessingException;
import com.sagag.services.rest.authorization.annotation.IsAccessibleUrlPreAuthorization;
import com.sagag.services.rest.resource.gtmotive.GtmotiveMultiPartSearchResource;
import com.sagag.services.rest.resource.gtmotive.GtmotivePartListSearchResponseResource;
import com.sagag.services.rest.resource.gtmotive.GtmotiveReferenceSearchResponseResource;
import com.sagag.services.rest.resource.gtmotive.GtmotiveVehicleSearchResponseResource;
import com.sagag.services.rest.swagger.docs.ApiDesc;
import com.sagag.services.service.api.GtmotiveBusinessService;
import com.sagag.services.service.response.gtmotive.GtmotivePartsListSearchResponse;
import com.sagag.services.service.response.gtmotive.GtmotiveReferenceSearchResponse;
import com.sagag.services.service.response.gtmotive.GtmotiveVehicleSearchResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.SocketTimeoutException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.xml.xpath.XPathExpressionException;

@RestController
@RequestMapping(value = "/gtmotive", produces = MediaType.APPLICATION_JSON_VALUE)
@GtmotiveProfile
@Api(tags = "GtMotive Integration APIs v2")
public class GtmotiveControllerV2 {

  @Autowired
  @Qualifier("gtmotiveBusinessServiceImpl")
  private GtmotiveBusinessService gtmotiveBusService;

  @Autowired
  private VinErrorLogService vinErrorLogService;

  @ApiOperation(value = ApiDesc.Gtmotive.GTMOTIVE_REFERENCES_SEARCH_API_DESC,
      notes = ApiDesc.Gtmotive.GTMOTIVE_REFERENCES_SEARCH_API_NOTE)
  @PostMapping(value = "/references/search", produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public GtmotiveReferenceSearchResponseResource searchReferencesByPartCode(
      final OAuth2Authentication authed,
      @RequestBody GtmotiveReferenceSearchByPartCodesCriteria criteria)
      throws XPathExpressionException, GtmotiveXmlResponseProcessingException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    criteria.setCustNr(user.getCustNrStr());
    criteria.setCompanyName(user.getCompanyName());
    criteria.setAffiliateShortName(user.getAffiliateShortName());
    GtmotiveReferenceSearchResponse gtmotiveResponse =
        gtmotiveBusService.searchReferencesByPartCode(criteria);
    GtmotiveReferenceSearchResponseResource resource =
        GtmotiveReferenceSearchResponseResource.of(gtmotiveResponse);
    resource.add(new Link("/references/search"));
    return resource;
  }

  @ApiOperation(value = ApiDesc.Gtmotive.GTMOTIVE_VEHICLE_INFO_BY_GT_INFO_API_DESC,
      notes = ApiDesc.Gtmotive.GTMOTIVE_VEHICLE_INFO_BY_GT_INFO_NOTE)
  @PostMapping(value = "/vehicle/search-by-gtinfo", produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public GtmotiveVehicleSearchResponseResource getGtVehicleInfoByGtInfo(
      final OAuth2Authentication authed,
      @RequestBody final GtmotiveVehicleSearchByGtInfoRequest request) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    GtmotiveVehicleSearchResponse vehicleResponse =
        gtmotiveBusService.searchVehicleByGtInfo(user.getCustNrStr(), request);
    GtmotiveVehicleSearchResponseResource resource =
        GtmotiveVehicleSearchResponseResource.of(vehicleResponse);
    resource.add(new Link("/vehicle/search-by-gtinfo"));
    return resource;
  }

  @ApiOperation(value = ApiDesc.Gtmotive.GTMOTIVE_VEHICLE_INFO_BY_VIN_API_DESC,
      notes = ApiDesc.Gtmotive.GTMOTIVE_VEHICLE_INFO_BY_VIN_API_NOTE)
  @PostMapping(value = "/vehicle/search-by-vin", produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  @IsAccessibleUrlPreAuthorization
  public GtmotiveVehicleSearchResponseResource getGtVehicleInfoByVin(
      final HttpServletRequest request,
      final OAuth2Authentication authed,
      @RequestBody final GtmotiveVehicleSearchByVinRequest requestBody)
      throws XPathExpressionException, GtmotiveXmlResponseProcessingException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    GtmotiveVehicleSearchResponse vehicleResponse =
        gtmotiveBusService.searchVehicleByVin(user.getCustNrStr(), requestBody);
    GtmotiveVehicleSearchResponseResource resource =
        GtmotiveVehicleSearchResponseResource.of(vehicleResponse);
    resource.add(new Link("/vehicle/search-by-vin"));
    return resource;
  }

  @ApiOperation(value = ApiDesc.Gtmotive.GTMOTIVE_GT_PART_LIST_SEARCH_API_DESC,
      notes = ApiDesc.Gtmotive.GTMOTIVE_GT_PART_LIST_SEARCH_API_NOTE)
  @PostMapping(value = "/part-list/search", produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public GtmotivePartListSearchResponseResource searchGtPartList(
      @RequestBody GtmotivePartsListSearchRequest searchRequest)
      throws XPathExpressionException, GtmotiveXmlResponseProcessingException {
    GtmotivePartsListSearchResponse response = gtmotiveBusService.searchGtPartsList(searchRequest);
    GtmotivePartListSearchResponseResource resource =
        GtmotivePartListSearchResponseResource.of(response);
    resource.add(new Link("/part-list/search"));
    return resource;
  }

  @ApiOperation(value = ApiDesc.Gtmotive.GET_GTMOTIVE_VIN_SECURITY_CHECK_DESC,
      notes = ApiDesc.Gtmotive.GET_GTMOTIVE_VIN_SECURITY_CHECK_NOTE)
  @PostMapping("/vin/security-check")
  public GtmotiveVinSecurityCheckResponse checkVinSecurity(final OAuth2Authentication authed,
      @RequestBody final GtmotiveVinSecurityCheckCriteria criteria)
      throws GtmotiveXmlResponseProcessingException, SocketTimeoutException,
      ConnectTimeoutException, ValidationException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return gtmotiveBusService.checkVinSecurity(user.getId(), user.getCustNrStr(), criteria);
  }

  @ApiOperation(value = ApiDesc.Gtmotive.GET_GTMOTIVE_MULTI_PART_SEARCH_DESC,
      notes = ApiDesc.Gtmotive.GET_GTMOTIVE_MULTI_PART_SEARCH_NOTE)
  @PostMapping("/multi-part/search")
  public GtmotiveMultiPartSearchResource searchMultiPart(
      @RequestBody final GtmotiveMultiPartSearchRequest request)
      throws XPathExpressionException, GtmotiveXmlResponseProcessingException {
    GtmotiveMultiPartSearchResponse response = gtmotiveBusService.searchMultiPart(request);
    GtmotiveMultiPartSearchResource resource = GtmotiveMultiPartSearchResource.of(response);
    resource.add(new Link("/multi-part/search"));
    return resource;
  }

  @ApiOperation(value = ApiDesc.Gtmotive.GTMOTIVE_VIN_SEARCH_ERROR_LOGGING_DESC,
      notes = ApiDesc.Gtmotive.GTMOTIVE_VIN_SEARCH_ERROR_LOGGING_NOTE)
  @PostMapping("/error/log")
  @ResponseStatus(value = HttpStatus.OK)
  public void logVinSearchError(@RequestBody final GtmotiveVinSearchErrorLoggingRequest request) {
    VinErrorLogDto dto =
        VinErrorLogDto.builder().vin(request.getVin()).umc(request.getUmc()).cupi(request.getCupi())
            .location(request.getLocation()).returnedData(request.getReturnedData())
            .type(request.getType()).oeNr(request.getOeNr()).createdDate(new Date()).build();
    vinErrorLogService.addVinErrorLog(dto);
  }

}
