package com.sagag.services.rest.controller.user_history;

import com.sagag.eshop.repo.criteria.user_history.UserVehicleHistorySearchCriteria;
import com.sagag.eshop.service.api.UserVehicleHistoryService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.domain.eshop.dto.VehicleHistoryDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for historical data.
 */
@RestController
@RequestMapping(value = "/history", produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Vehicle History API")
public class VehicleHistoryController {

  @Autowired
  private UserVehicleHistoryService userVehHistoryService;

  private static final String GET_VEHICLE_HISTORY_API_DESC = "Get vehicle history";
  private static final String GET_VEHICLE_HISTORY_API_NOTE = "The service will get vehicle history";

  //@formatter:off
  @ApiOperation(
      value = GET_VEHICLE_HISTORY_API_DESC,
      notes = GET_VEHICLE_HISTORY_API_NOTE
  )
  @GetMapping(
      value = "/vehicle",
      produces = MediaType.APPLICATION_JSON_VALUE
  ) //@formatter:on
  public Page<VehicleHistoryDto> getVehicleHistory(final OAuth2Authentication authed,
      @RequestParam(value = "vehicleClass",
          defaultValue = StringUtils.EMPTY) final String vehicleClass) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return userVehHistoryService.filterLastestVehicleHistoryByVehicleClass(user.getOriginalUserId(),
        vehicleClass);
  }

  @PostMapping("/vehicles")
  public Page<VehicleHistoryDto> searchVehicleHistories(final OAuth2Authentication authed,
      @RequestBody final UserVehicleHistorySearchCriteria criteria,
      @PageableDefault final Pageable pageable) throws ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    if (user.isFinalUserRole()) {
      criteria.setOrgId(user.getFinalCustomerOrgId());
    } else {
      criteria.setOrgId(user.getOrganisationId());
    }

    criteria.setUserId(user.getOriginalUserId());
    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(
        userVehHistoryService.searchVehicleHistories(criteria, pageable));
  }
}
