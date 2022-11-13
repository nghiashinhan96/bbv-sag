package com.sagag.services.admin.controller;

import com.sagag.eshop.service.api.LicenseService;
import com.sagag.eshop.service.exception.LicenseExportException;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.domain.eshop.backoffice.dto.BackOfficeLicenseDto;
import com.sagag.services.domain.eshop.criteria.LicenseSearchCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin/licenses", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "admin")
public class AdminLicenseController {

  @Autowired
  private LicenseService licenseService;

  /**
   * Searches the licenses by search criteria.
   *
   * @param searchCriteria the search criteria
   * @return Page<LicenseSearchResultItemDto>
   */
  @ApiOperation(value = "Search licenses for back office", notes = "Search by criteria")
  @PostMapping(value = "/search")
  public Page<BackOfficeLicenseDto> search(@RequestBody final LicenseSearchCriteria searchCriteria) {
    return licenseService.search(searchCriteria);
  }

  @PostMapping(value = "/export-csv")
  public ResponseEntity<byte[]> exportCsv(@RequestBody final LicenseSearchCriteria searchCriteria)
      throws ServiceException {
    return licenseService.exportToCsvByCriteria(searchCriteria).buildResponseEntity();
  }

  @PostMapping(value = "/export-excel")
  public ResponseEntity<byte[]> exportExcel(@RequestBody final LicenseSearchCriteria searchCriteria)
      throws LicenseExportException {
    return licenseService.exportToExcelByCriteria(searchCriteria).buildResponseEntity();
  }
}
