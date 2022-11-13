package com.sagag.services.admin.controller;

import com.sagag.eshop.service.api.AadAccountsService;
import com.sagag.eshop.service.exception.DuplicatedEmailException;
import com.sagag.services.admin.swagger.docs.ApiDesc;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.domain.eshop.criteria.AadAccountsSearchRequestCriteria;
import com.sagag.services.domain.eshop.dto.AadAccountsDto;
import com.sagag.services.domain.eshop.dto.AadAccountsSearchResultDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/aad-account")
@Api(tags = "admin")
public class AadAccountsController {

  @Autowired
  private AadAccountsService aadAccountsService;

  @ApiOperation(value = ApiDesc.AadAccounts.CREATE_NEW_AAD_ACCOUNT_DESC,
      notes = ApiDesc.AadAccounts.CREATE_NEW_AAD_ACCOUNT_NOTE)
  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public void create(@RequestBody final AadAccountsDto dto)
      throws DuplicatedEmailException, ValidationException {
    aadAccountsService.create(dto);
  }

  @ApiOperation(value = ApiDesc.AadAccounts.UPDATE_AAD_ACCOUNT_DESC,
      notes = ApiDesc.AadAccounts.UPDATE_AAD_ACCOUNT_NOTE)
  @PutMapping(value = "/{id}/update", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public void update(@RequestBody final AadAccountsDto dto, @PathVariable("id") Integer id)
      throws DuplicatedEmailException, ValidationException {
    aadAccountsService.update(dto, id);
  }

  @ApiOperation(value = ApiDesc.AadAccounts.SEARCH_AAD_ACCOUNT_DESC,
      notes = ApiDesc.AadAccounts.SEARCH_AAD_ACCOUNT_NOTE)
  @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public Page<AadAccountsSearchResultDto> search(
      @RequestBody AadAccountsSearchRequestCriteria criteria,
      @PageableDefault final Pageable pageable) throws ResultNotFoundException {
    return RestExceptionUtils
        .doSafelyReturnNotEmptyRecords(aadAccountsService.search(criteria, pageable));
  }

  @ApiOperation(value = ApiDesc.AadAccounts.FIND_AAD_ACCOUNT_BY_ID_DESC,
      notes = ApiDesc.AadAccounts.FIND_AAD_ACCOUNT_BY_ID_NOTE)
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public AadAccountsSearchResultDto findById(@PathVariable("id") final Integer id) {
    return aadAccountsService.findById(id);
  }
}
