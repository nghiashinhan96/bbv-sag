package com.sagag.services.rest.controller.finalcustomer;

import com.sagag.eshop.repo.entity.Country;
import com.sagag.eshop.repo.entity.WssWorkingDay;
import com.sagag.eshop.service.api.CountryService;
import com.sagag.eshop.service.api.WssOpeningDaysService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.WssOpeningDaysValidationException;
import com.sagag.eshop.service.exception.WssOpeningDaysValidationException.WssOpeningDaysErrorCase;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.exception.CsvServiceException;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.utils.CsvUtils;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.domain.eshop.criteria.WssOpeningDaysSearchCriteria;
import com.sagag.services.domain.eshop.dto.WssCsvOpeningCalendar;
import com.sagag.services.domain.eshop.dto.WssOpeningDaysDetailDto;
import com.sagag.services.domain.eshop.dto.WssOpeningDaysDto;
import com.sagag.services.domain.eshop.openingdays.dto.WssOpeningDaysRequestBody;
import com.sagag.services.rest.authorization.annotation.HasWholesalerPreAuthorization;
import com.sagag.services.rest.swagger.docs.ApiDesc;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.collections.CollectionUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * The controller exposes API for WSS opening days calendar.
 */
@RestController
@RequestMapping("/wss/opening-days")
@Api(tags = "WSS Opening Calendar APIs")
public class WssOpeningDaysCalendarController {

  @Autowired
  private WssOpeningDaysService wssOpeningDaysService;

  @Autowired CountryService countryService;

  @ApiOperation(value = ApiDesc.WssOpeningDaysCalendar.GET_WSS_WORKING_DAY_CODES_DESC,
      notes = ApiDesc.WssOpeningDaysCalendar.GET_WSS_WORKING_DAY_CODES_NOTE)
  @GetMapping(value = "/working-day-code", produces = MediaType.APPLICATION_JSON_VALUE)
  @HasWholesalerPreAuthorization
  public List<WssWorkingDay> getWorkingDayCodes() throws ResultNotFoundException {
    return RestExceptionUtils
        .doSafelyReturnNotEmptyRecords(wssOpeningDaysService.getWorkingDayCodes());
  }

  @ApiOperation(value = ApiDesc.WssOpeningDaysCalendar.CREATE_NEW_WSS_OPENING_DAYS_CALENDAR_DESC,
      notes = ApiDesc.WssOpeningDaysCalendar.CREATE_NEW_WSS_OPENING_DAYS_CALENDAR_NOTE)
  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  @HasWholesalerPreAuthorization
  public WssOpeningDaysDetailDto create(OAuth2Authentication authed, @RequestBody final WssOpeningDaysRequestBody request)
      throws WssOpeningDaysValidationException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return wssOpeningDaysService.create(request, user.getOrganisationId());
  }

  @ApiOperation(value = ApiDesc.WssOpeningDaysCalendar.UPDATE_WSS_OPENING_DAYS_CALENDAR_DESC,
      notes = ApiDesc.WssOpeningDaysCalendar.UPDATE_WSS_OPENING_DAYS_CALENDAR_NOTE)
  @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
  @HasWholesalerPreAuthorization
  public WssOpeningDaysDetailDto update(OAuth2Authentication authed, @RequestBody final WssOpeningDaysRequestBody request)
      throws WssOpeningDaysValidationException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return wssOpeningDaysService.update(request, user.getOrganisationId());
  }

  @ApiOperation(value = ApiDesc.WssOpeningDaysCalendar.REMOVE_WSS_OPENING_DAYS_CALENDAR_DESC,
      notes = ApiDesc.WssOpeningDaysCalendar.REMOVE_WSS_OPENING_DAYS_CALENDAR_NOTE)
  @DeleteMapping(value = "/remove/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  @HasWholesalerPreAuthorization
  public void remove(@PathVariable("id") final Integer id) {
    wssOpeningDaysService.remove(id);
  }

  @ApiOperation(value = ApiDesc.WssOpeningDaysCalendar.GET_WSS_OPENING_DAYS_CALENDAR_DETAIL_DESC,
      notes = ApiDesc.WssOpeningDaysCalendar.GET_WSS_OPENING_DAYS_CALENDAR_DETAIL_NOTE)
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @HasWholesalerPreAuthorization
  public WssOpeningDaysDetailDto getWssOpeningDaysCalendarById(@PathVariable("id") final Integer id)
      throws ResultNotFoundException {
    return RestExceptionUtils
        .doSafelyReturnOptionalRecord(wssOpeningDaysService.getOpeningDaysCalendar(id));
  }

  @ApiOperation(value = ApiDesc.WssOpeningDaysCalendar.SEARCH_WSS_OPENING_DAYS_CALENDAR_DESC,
      notes = ApiDesc.WssOpeningDaysCalendar.SEARCH_WSS_OPENING_DAYS_CALENDAR_NOTE)
  @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  @HasWholesalerPreAuthorization
  public Page<WssOpeningDaysDto> searchVehiclesByRequest(OAuth2Authentication authed,
      @RequestBody final WssOpeningDaysSearchCriteria criteria,
      @PageableDefault final Pageable pageable) throws ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    criteria.setOrgId(user.getOrganisationId());
    return RestExceptionUtils
        .doSafelyReturnNotEmptyRecords(wssOpeningDaysService.searchByCriteria(criteria, pageable));
  }

  /**
   * Imports all record from csv file into DB
   *
   * @param file
   * @param checkExisting param to check allow override all existing data
   * @throws WssOpeningDaysValidationException
   * @throws CsvServiceException
   */
  @ApiOperation(value = ApiDesc.WssOpeningDaysCalendar.IMPORT_WSS_OPENING_DAYS_CALENDAR_FROM_CSV_DESC,
      notes = ApiDesc.WssOpeningDaysCalendar.IMPORT_WSS_OPENING_DAYS_CALENDAR_FROM_CSV_NOTE)
  @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @HasWholesalerPreAuthorization
  public void importFromCsv(OAuth2Authentication authed, @RequestParam("file") MultipartFile file,
      @RequestParam(value = "checkExisting", required = false) boolean checkExisting)
      throws WssOpeningDaysValidationException, CsvServiceException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    final boolean useDefaultCharset = false;
    final List<WssCsvOpeningCalendar> wssOpeningDaysCalendar =
        CsvUtils.read(file, WssCsvOpeningCalendar.class, SagConstants.CSV_DEFAULT_SEPARATOR, useDefaultCharset);
    if (CollectionUtils.isEmpty(wssOpeningDaysCalendar)) {
      throw new WssOpeningDaysValidationException(WssOpeningDaysErrorCase.WODE_EMP_003,
          "The imported WSS opening days calendar data is empty");
    }
    wssOpeningDaysService.importWssOpeningDays(wssOpeningDaysCalendar, checkExisting, user.getOrganisationId());
  }

  @ApiOperation(value = ApiDesc.WssOpeningDaysCalendar.GET_COUNTRY_INFO_BY_CODE_DESC,
      notes = ApiDesc.WssOpeningDaysCalendar.GET_COUNTRY_INFO_BY_CODE_NOTE)
  @GetMapping(value = "/countries/{code}")
  @HasWholesalerPreAuthorization
  public List<Country> getCountriesByCode(@PathVariable("code") final String code)
      throws ResultNotFoundException {
    return RestExceptionUtils
        .doSafelyReturnNotEmptyRecords(countryService.getCountriesByCode(code));
  }
}
