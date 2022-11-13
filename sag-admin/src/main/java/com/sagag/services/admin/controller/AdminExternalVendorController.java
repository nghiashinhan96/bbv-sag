package com.sagag.services.admin.controller;

import com.sagag.eshop.repo.api.ExternalVendorRepository;
import com.sagag.eshop.repo.entity.ExternalVendor;
import com.sagag.eshop.repo.entity.VExternalVendor;
import com.sagag.eshop.service.dto.CsvExternalVendorDto;
import com.sagag.eshop.service.exception.ExternalVendorValidationException;
import com.sagag.eshop.service.exception.ExternalVendorValidationException.ExternalVendorErrorCase;
import com.sagag.services.admin.swagger.docs.ApiDesc;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.exception.CsvServiceException;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.utils.CsvUtils;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.domain.eshop.criteria.ExternalVendorSearchCriteria;
import com.sagag.services.domain.eshop.dto.externalvendor.ExternalVendorDto;
import com.sagag.services.domain.eshop.dto.externalvendor.SupportExternalVendorDto;
import com.sagag.services.service.api.ExternalVendorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/external-vendor")
@Api(tags = "Admin External Vendor Management APIs")
public class AdminExternalVendorController {

  @Autowired
  private ExternalVendorService externalVendorService;

  @Autowired
  private ExternalVendorRepository externalVendorRepo;

  @ApiOperation(value = ApiDesc.AdminExternalVendor.IMPORT_EXTERNAL_VENDOR,
      notes = ApiDesc.AdminExternalVendor.IMPORT_EXTERNAL_VENDOR_NOTE)
  @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public void importExternalVendor(@RequestParam("file") MultipartFile file)
      throws ExternalVendorValidationException, CsvServiceException {
    final boolean useDefaultCharset = false;
    final List<CsvExternalVendorDto> externalVendors =
        CsvUtils.read(file, CsvExternalVendorDto.class, SagConstants.CSV_DEFAULT_SEPARATOR, useDefaultCharset);
    externalVendorService.importExternalVendor(externalVendors);
  }

  @ApiOperation(value = ApiDesc.AdminExternalVendor.SEARCH_EXTERNAL_VENDOR,
      notes = ApiDesc.AdminExternalVendor.SEARCH_EXTERNAL_VENDOR_NOTE)
  @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public Page<VExternalVendor> searchExternalVendor(
      @RequestBody final ExternalVendorSearchCriteria searchCriteria)
      throws ResultNotFoundException {
    return RestExceptionUtils
        .doSafelyReturnNotEmptyRecords(externalVendorService.searchExternalVendor(searchCriteria));
  }

  @ApiOperation(value = ApiDesc.AdminExternalVendor.CREATE_MASTER_DATA_EXTERNAL_VENDOR,
      notes = ApiDesc.AdminExternalVendor.CREATE_MASTER_DATA_EXTERNAL_VENDOR_NOTE)
  @GetMapping(value = "/init-data", produces = MediaType.APPLICATION_JSON_VALUE)
  public SupportExternalVendorDto getMasterDataExternalVendor() {
    return externalVendorService.getMasterDataExternalVendor();
  }

  @ApiOperation(value = ApiDesc.AdminExternalVendor.CREATE_EXTERNAL_VENDOR,
      notes = ApiDesc.AdminExternalVendor.CREATE_EXTERNAL_VENDOR_NOTE)
  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public void createExternalVendor(@Valid @RequestBody final ExternalVendorDto request,
      final OAuth2Authentication authed) throws ExternalVendorValidationException {
    final Long userId = Long.parseLong(authed.getPrincipal().toString());
    externalVendorService.createExternalVendor(request, userId);
  }

  @ApiOperation(value = ApiDesc.AdminExternalVendor.EDIT_EXTERNAL_VENDOR,
      notes = ApiDesc.AdminExternalVendor.EDIT_EXTERNAL_VENDOR_NOTE)
  @PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public void update(@RequestBody final ExternalVendorDto externalVendorRequest,
      final OAuth2Authentication authed) throws ExternalVendorValidationException {
    final Long userId = Long.parseLong(authed.getPrincipal().toString());
    externalVendorService.updateExternalVendor(externalVendorRequest, userId);
  }

  @ApiOperation(value = ApiDesc.AdminExternalVendor.DELETE_EXTERNAL_VENDOR,
      notes = ApiDesc.AdminExternalVendor.DELETE_EXTERNAL_VENDOR_NOTE)
  @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public void delete(@PathVariable("id") final Integer id)
      throws ExternalVendorValidationException {
    externalVendorService.delete(id);
  }

  @ApiOperation(value = ApiDesc.AdminExternalVendor.FIND_EXTERNAL_VENDOR,
      notes = ApiDesc.AdminExternalVendor.FIND_EXTERNAL_VENDOR_NOTE)
  @GetMapping(value = "/{id}/find", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ExternalVendor findExternalVendor(@PathVariable("id") final Integer id)
      throws ExternalVendorValidationException {
    return externalVendorRepo.findById(id).orElseThrow(
        () -> new ExternalVendorValidationException(ExternalVendorErrorCase.ODE_EMP_003,
            String.format("The external vendor %d cannot find ", id)));
  }
}
