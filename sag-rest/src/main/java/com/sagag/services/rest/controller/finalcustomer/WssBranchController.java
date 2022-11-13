package com.sagag.services.rest.controller.finalcustomer;

import com.sagag.eshop.service.api.WssBranchService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.WssBranchValidationException;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.domain.eshop.branch.dto.WssBranchRequestBody;
import com.sagag.services.domain.eshop.branch.dto.WssBranchSearchRequestCriteria;
import com.sagag.services.domain.eshop.dto.WssBranchDto;
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

import java.util.List;

/**
 * The controller exposes API for WSS customer branch.
 *
 */
@RestController
@RequestMapping("/wss/branch")
@Api(tags = "WSS Branch Opening Time APIs")
public class WssBranchController {

  @Autowired
  private WssBranchService wssBranchService;

  @ApiOperation(value = ApiDesc.WssBranch.CREATE_NEW_WSS_BRANCH_DESC,
      notes = ApiDesc.WssBranch.CREATE_NEW_WSS_BRANCH_NOTE)
  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  @HasWholesalerPreAuthorization
  public WssBranchDto create(OAuth2Authentication authed, @RequestBody final WssBranchRequestBody request)
      throws WssBranchValidationException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return wssBranchService.create(request, user.getOrganisationId());
  }

  @ApiOperation(value = ApiDesc.WssBranch.UPDATE_WSS_BRANCH_DESC,
      notes = ApiDesc.WssBranch.UPDATE_WSS_BRANCH_NOTE)
  @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
  @HasWholesalerPreAuthorization
  public WssBranchDto update(OAuth2Authentication authed, @RequestBody final WssBranchRequestBody request)
      throws WssBranchValidationException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return wssBranchService.update(request, user.getOrganisationId());
  }

  @ApiOperation(value = ApiDesc.WssBranch.REMOVE_WSS_BRANCH_DESC,
      notes = ApiDesc.WssBranch.REMOVE_WSS_BRANCH_NOTE)
  @DeleteMapping(value = "/remove/{branchNr}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  @HasWholesalerPreAuthorization
  public void remove(OAuth2Authentication authed, @PathVariable("branchNr") final Integer branchNr)
      throws WssBranchValidationException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    wssBranchService.remove(branchNr, user.getOrganisationId());
  }

  @ApiOperation(value = ApiDesc.WssBranch.SEARCH_WSS_BRANCH_DESC,
      notes = ApiDesc.WssBranch.SEARCH_WSS_BRANCH_NOTE)
  @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  @HasWholesalerPreAuthorization
  public Page<WssBranchDto> searchBranch(OAuth2Authentication authed, @RequestBody final WssBranchSearchRequestCriteria criteria,
      @PageableDefault final Pageable pageable) throws ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    criteria.setOrgId(user.getOrganisationId());
    return RestExceptionUtils
        .doSafelyReturnNotEmptyRecords(wssBranchService.searchBranchByCriteria(criteria, pageable));
  }

  @ApiOperation(value = ApiDesc.WssBranch.GET_WSS_BRANCH_DETAIL_DESC,
      notes = ApiDesc.WssBranch.GET_WSS_BRANCH_DETAIL_NOTE)
  @GetMapping(value = "/{branchNr}", produces = MediaType.APPLICATION_JSON_VALUE)
  @HasWholesalerPreAuthorization
  public WssBranchDto getBranchByBranNr(OAuth2Authentication authed, @PathVariable("branchNr") final Integer branchNr)
      throws WssBranchValidationException, ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return RestExceptionUtils.doSafelyReturnOptionalRecord(wssBranchService.getBranchDetail(branchNr, user.getOrganisationId()));
  }

  @ApiOperation(value = ApiDesc.WssBranch.GET_WSS_BRANCH_LIST_DESC,
      notes = ApiDesc.WssBranch.GET_WSS_BRANCH_LIST_NOTE)
  @GetMapping(value = "/branches", produces = MediaType.APPLICATION_JSON_VALUE)
  @HasWholesalerPreAuthorization
  public List<WssBranchDto> getBranches(OAuth2Authentication authed) throws ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(wssBranchService.getBranchesByOrganisation(user.getOrganisationId()));
  }

}
