package com.sagag.services.rest.controller.integration;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.gtmotive.config.GtmotiveProfile;
import com.sagag.services.gtmotive.domain.request.GtmotiveVehicleCriteria;
import com.sagag.services.ivds.request.gtmotive.GtmotiveOperationRequest;
import com.sagag.services.ivds.response.GtmotiveResponse;
import com.sagag.services.rest.swagger.docs.ApiDesc;
import com.sagag.services.service.api.GtmotiveBusinessService;
import com.sagag.services.service.resource.gtmotive.GtmotiveResponseResource;
import com.sagag.services.service.resource.gtmotive.GtmotiveVehicleResource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * GTmotive Controller APIs class.
 */
@RestController
@RequestMapping(value = "/gtmotive", produces = MediaType.APPLICATION_JSON_VALUE)
@GtmotiveProfile
@Api(tags = "GtMotive Integration APIs")
public class GtmotiveController {

  @Autowired
  private GtmotiveBusinessService gtmotiveBusService;

  /**
   * Returns the GTMotive vehicle info by VIN.
   *
   * @param authed the user who requests.
   * @param criteria the vin code request param
   * @return the found GTMotive vehicle info
   * @throws ResultNotFoundException throws when not found any vehicles
   */
  @ApiOperation(
    value = ApiDesc.Gtmotive.GTMOTIVE_VEHICLE_INFO_BY_VIN_API_DESC,
    notes = ApiDesc.Gtmotive.GTMOTIVE_VEHICLE_INFO_BY_VIN_API_NOTE
  )
  @PostMapping("/vehicle")
  public GtmotiveVehicleResource getGtVehicleInfo(final OAuth2Authentication authed,
    @RequestBody final GtmotiveVehicleCriteria criteria) throws ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    Optional<GtmotiveVehicleResource> response = gtmotiveBusService.searchVehicleByGtmotive(
        user.getCustNrStr(), criteria);
    return RestExceptionUtils.doSafelyReturnOptionalRecord(response);
  }

  /**
   * Returns the selected parts from the Gtmotive References.
   *
   * @param authed the user who requests.
   * @param request the request of gtmotive reference codes
   * @return the result of {@link GtmotiveResponseResource}
   */
  @PostMapping("/articles")
  public GtmotiveResponseResource searchArticlesByGtReferences(final OAuth2Authentication authed,
      @RequestBody GtmotiveOperationRequest request) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return doSearchArticlesByGtReferences(user, request);
  }

  /**
   * Returns the selected parts from the Gtmotive References v2.
   *
   * @param authed the user who requests.
   * @param request the request of gtmotive reference codes
   * @return the result of {@link GtmotiveResponseResource}
   */
  @PostMapping("/v2/articles")
  public GtmotiveResponseResource searchArticlesByGtReferencesV2(final OAuth2Authentication authed,
      @RequestBody GtmotiveOperationRequest request) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    request.setUsingVersion2(true);
    return doSearchArticlesByGtReferences(user, request);
  }

  private GtmotiveResponseResource doSearchArticlesByGtReferences(UserInfo user,
      GtmotiveOperationRequest request) {
    final GtmotiveResponse response =
        gtmotiveBusService.searchArticlesByGtOperations(user, request);
    final GtmotiveResponseResource resource = GtmotiveResponseResource.of(response);
    resource.add(new Link("/articles").withSelfRel());
    return resource;
  }
}
