package com.sagag.services.admin.controller;

import com.sagag.eshop.repo.entity.WorkingDay;
import com.sagag.eshop.service.api.OpeningDaysService;
import com.sagag.eshop.service.dto.CsvOpeningCalendar;
import com.sagag.eshop.service.dto.OpeningDaysDetailDto;
import com.sagag.eshop.service.dto.OpeningDaysDto;
import com.sagag.eshop.service.exception.OpeningDaysValidationException;
import com.sagag.eshop.service.exception.OpeningDaysValidationException.OpeningDaysErrorCase;
import com.sagag.services.admin.swagger.docs.ApiDesc;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.exception.CsvServiceException;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.utils.CsvUtils;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.domain.eshop.criteria.OpeningDaysSearchCriteria;
import com.sagag.services.domain.eshop.openingdays.dto.OpeningDaysRequestBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
 * The controller exposes api for opening days calendar.
 */
@RestController
@RequestMapping("/admin/opening-days")
@Api(tags = "admin")
public class OpeningDaysCalendarController {

  @Autowired
  private OpeningDaysService openingDaysService;

  @ApiOperation(value = ApiDesc.OpeningDaysCalendar.GET_WORKING_DAY_CODES_DESC,
      notes = ApiDesc.OpeningDaysCalendar.GET_WORKING_DAY_CODES_NOTE)
  @GetMapping(value = "/working-day-code", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<WorkingDay> getWorkingDayCodes() throws ResultNotFoundException {
    return RestExceptionUtils
        .doSafelyReturnNotEmptyRecords(openingDaysService.getWorkingDayCodes());
  }

  @ApiOperation(value = ApiDesc.OpeningDaysCalendar.CREATE_NEW_OPENING_DAYS_CALENDAR_DESC,
      notes = ApiDesc.OpeningDaysCalendar.CREATE_NEW_OPENING_DAYS_CALENDAR_NOTE)
  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  public OpeningDaysDetailDto create(@RequestBody final OpeningDaysRequestBody request)
      throws OpeningDaysValidationException {
    return openingDaysService.create(request);
  }

  @ApiOperation(value = ApiDesc.OpeningDaysCalendar.UPDATE_OPENING_DAYS_CALENDAR_DESC,
      notes = ApiDesc.OpeningDaysCalendar.UPDATE_OPENING_DAYS_CALENDAR_NOTE)
  @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
  public OpeningDaysDetailDto update(@RequestBody final OpeningDaysRequestBody request)
      throws OpeningDaysValidationException {
    return openingDaysService.update(request);
  }

  @ApiOperation(value = ApiDesc.OpeningDaysCalendar.REMOVE_OPENING_DAYS_CALENDAR_DESC,
      notes = ApiDesc.OpeningDaysCalendar.REMOVE_OPENING_DAYS_CALENDAR_NOTE)
  @DeleteMapping(value = "/remove/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public void remove(@PathVariable("id") final Integer id) {
    openingDaysService.remove(id);
  }

  @ApiOperation(value = ApiDesc.OpeningDaysCalendar.GET_OPENING_DAYS_CALENDAR_DETAIL_DESC,
      notes = ApiDesc.OpeningDaysCalendar.CREATE_NEW_OPENING_DAYS_CALENDAR_NOTE)
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public OpeningDaysDetailDto getOpeningDaysCalendarById(@PathVariable("id") final Integer id)
      throws ResultNotFoundException {
    return RestExceptionUtils
        .doSafelyReturnOptionalRecord(openingDaysService.getOpeningDaysCalendar(id));
  }

  @ApiOperation(value = ApiDesc.OpeningDaysCalendar.SEARCH_OPENING_DAYS_CALENDAR_DESC,
      notes = ApiDesc.OpeningDaysCalendar.SEARCH_OPENING_DAYS_CALENDAR_NOTE)
  @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public Page<OpeningDaysDto> searchVehiclesByRequest(
      @RequestBody final OpeningDaysSearchCriteria criteria,
      @PageableDefault final Pageable pageable) throws ResultNotFoundException {
    return RestExceptionUtils
        .doSafelyReturnNotEmptyRecords(openingDaysService.searchByCriteria(criteria, pageable));
  }

  /**
   * Imports all record from csv file into DB
   *
   * @param file
   * @param checkExisting param to check allow override all existing data
   * @throws OpeningDaysValidationException
   * @throws CsvServiceException
   */
  @ApiOperation(value = ApiDesc.OpeningDaysCalendar.IMPORT_OPENING_DAYS_CALENDAR_FROM_CSV_DESC,
      notes = ApiDesc.OpeningDaysCalendar.IMPORT_OPENING_DAYS_CALENDAR_FROM_CSV_NOTE)
  @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public void importFromCsv(@RequestParam("file") MultipartFile file,
      @RequestParam(value = "checkExisting", required = false) boolean checkExisting)
      throws OpeningDaysValidationException, CsvServiceException {
    final boolean useDefaultCharset = false;
    final List<CsvOpeningCalendar> openingDaysCalendar =
        CsvUtils.read(file, CsvOpeningCalendar.class, SagConstants.CSV_DEFAULT_SEPARATOR, useDefaultCharset);
    if (CollectionUtils.isEmpty(openingDaysCalendar)) {
      throw new OpeningDaysValidationException(OpeningDaysErrorCase.ODE_EMP_003,
          "The imported opening days calendar data is empty");
    }
    openingDaysService.importOpeningDays(openingDaysCalendar, checkExisting);
  }
}
