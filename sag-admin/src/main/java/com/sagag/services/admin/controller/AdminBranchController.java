package com.sagag.services.admin.controller;

import com.sagag.eshop.service.api.BranchService;
import com.sagag.eshop.service.dto.BranchDetailDto;
import com.sagag.eshop.service.dto.BranchDto;
import com.sagag.eshop.service.exception.BranchValidationException;
import com.sagag.services.admin.swagger.docs.ApiDesc;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.domain.eshop.branch.dto.BranchRequestBody;
import com.sagag.services.domain.eshop.branch.dto.BranchSearchRequestCriteria;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The controller exposes api for customer branch.
 *
 */
@RestController
@RequestMapping("/admin/branch")
@Api(tags = "admin")
public class AdminBranchController {

  @Autowired
  private BranchService branchService;

  @ApiOperation(value = ApiDesc.Branch.CREATE_NEW_BRANCH_DESC,
      notes = ApiDesc.Branch.CREATE_NEW_BRANCH_NOTE)
  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  public BranchDetailDto create(@RequestBody final BranchRequestBody request)
      throws BranchValidationException {
    return branchService.create(request);
  }

  @ApiOperation(value = ApiDesc.Branch.UPDATE_BRANCH_DESC,
      notes = ApiDesc.Branch.UPDATE_BRANCH_NOTE)
  @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
  public BranchDetailDto update(@RequestBody final BranchRequestBody request)
      throws BranchValidationException {
    return branchService.update(request);
  }

  @ApiOperation(value = ApiDesc.Branch.REMOVE_BRANCH_DESC,
      notes = ApiDesc.Branch.REMOVE_BRANCH_NOTE)
  @DeleteMapping(value = "/remove/{branchNr}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public void remove(@PathVariable("branchNr") final Integer branchNr)
      throws BranchValidationException {
    branchService.remove(branchNr);
  }

  @ApiOperation(value = ApiDesc.Branch.SEARCH_BRANCH_DESC,
      notes = ApiDesc.Branch.SEARCH_BRANCH_NOTE)
  @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public Page<BranchDto> searchBranch(@RequestBody final BranchSearchRequestCriteria criteria,
      @PageableDefault final Pageable pageable) throws ResultNotFoundException {
    return RestExceptionUtils
        .doSafelyReturnNotEmptyRecords(branchService.searchBranchByCriteria(criteria, pageable));
  }

  @ApiOperation(value = ApiDesc.Branch.GET_BRANCH_DETAIL_DESC,
      notes = ApiDesc.Branch.GET_BRANCH_DETAIL_NOTE)
  @GetMapping(value = "/{branchNr}", produces = MediaType.APPLICATION_JSON_VALUE)
  public BranchDetailDto getBranchByBranNr(@PathVariable("branchNr") final Integer branchNr)
      throws BranchValidationException, ResultNotFoundException {
    return RestExceptionUtils.doSafelyReturnOptionalRecord(branchService.getBranchDetail(branchNr));
  }

  @ApiOperation(value = ApiDesc.Branch.GET_BRANCH_LIST_DESC,
      notes = ApiDesc.Branch.GET_BRANCH_LIST_NOTE)
  @GetMapping(value = "/branches", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<BranchDto> getBranches() throws ResultNotFoundException {
    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(branchService.getBranches());
  }

  @ApiOperation(value = ApiDesc.Branch.GET_BRANCHES_BY_COUNTRY_DESC,
      notes = ApiDesc.Branch.GET_BRANCHES_BY_COUNTRY_NOTE)
  @GetMapping(value = "/branches/{countryShortCode}", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<BranchDto> getBranchesByCountry(
      @PathVariable("countryShortCode") final String countryShortCode)
      throws ResultNotFoundException, BranchValidationException {
    return RestExceptionUtils
        .doSafelyReturnNotEmptyRecords(branchService.getBranchesByCountry(countryShortCode));
  }
}
