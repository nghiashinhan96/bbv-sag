package com.sagag.services.rest.controller.finalcustomer;

import com.sagag.eshop.repo.criteria.finalcustomer.FinalCustomerSearchCriteria;
import com.sagag.eshop.service.api.FinalCustomerService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.finalcustomer.FinalCustomerDto;
import com.sagag.eshop.service.dto.finalcustomer.NewFinalCustomerDto;
import com.sagag.eshop.service.dto.finalcustomer.SavingFinalCustomerDto;
import com.sagag.eshop.service.dto.finalcustomer.TemplateNewFinalCustomerProfileDto;
import com.sagag.eshop.service.dto.finalcustomer.UpdatingFinalCustomerDto;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.rest.authorization.annotation.HasWholesalerPreAuthorization;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Controller class to provide RESTful APIs for final customers.
 */
@RestController
@RequestMapping(value = "/final-customer", produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Final Customer APIs")
public class FinalCustomerController {

  @Autowired
  private FinalCustomerService finalCustomerService;

  @ApiOperation(value = "The API to search final customer info belong to current customer.")
  @PostMapping("/search")
  @HasWholesalerPreAuthorization
  public Page<FinalCustomerDto> searchFinalCustomersByCriteria(OAuth2Authentication authed,
      @RequestBody FinalCustomerSearchCriteria criteria, @PageableDefault Pageable pageable)
      throws ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    final Page<FinalCustomerDto> finalCustomers = finalCustomerService
        .searchFinalCustomersBelongToCustomer(user.getOrganisationId(), criteria, pageable);
    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(finalCustomers);
  }

  @ApiOperation(value = "The API to get selected final customer info.")
  @GetMapping("/{finalCustomerOrgId}/info")
  @HasWholesalerPreAuthorization
  public FinalCustomerDto getFinalCustomerInfo(OAuth2Authentication authed,
      @ApiParam @PathVariable(required = false) Integer finalCustomerOrgId,
      @ApiParam @RequestParam(name = "fullMode", defaultValue = "false") boolean fullMode)
      throws ResultNotFoundException {
    final Optional<FinalCustomerDto> finalCustomer =
        finalCustomerService.getFinalCustomerInfo(finalCustomerOrgId, fullMode);
    return RestExceptionUtils.doSafelyReturnOptionalRecord(finalCustomer);
  }

  @ApiOperation(value = "The API to get selected final customer info.")
  @GetMapping("info")
  public FinalCustomerDto getFinalCustomerInfo(OAuth2Authentication authed,
      @ApiParam @RequestParam(name = "fullMode", defaultValue = "false") boolean fullMode)
      throws ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    final Optional<FinalCustomerDto> finalCustomer =
        finalCustomerService.getFinalCustomerInfo(user.getFinalCustomerOrgId(), fullMode);
    return RestExceptionUtils.doSafelyReturnOptionalRecord(finalCustomer);
  }

  @ApiOperation(value = "The API to provice master data for creating new final customer.")
  @GetMapping("/profile/template")
  @HasWholesalerPreAuthorization
  public TemplateNewFinalCustomerProfileDto getTemplateNewFinalCustomerProfile(
      final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return finalCustomerService.getTemplateNewFinalCustomerProfile(user.getOrganisationId(),
        user.getId(), user.isShowNetPriceEnabled());
  }

  @ApiOperation(value = "The API to create new final customer")
  @PostMapping("/create")
  @ResponseStatus(value = HttpStatus.OK)
  @HasWholesalerPreAuthorization
  public void createFinalCustomer(final OAuth2Authentication authed,
      @RequestBody NewFinalCustomerDto newFinalCustomerDto) throws ServiceException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    newFinalCustomerDto.setCustomerOrgId(user.getOrganisationId());
    finalCustomerService.createFinalCustomer(newFinalCustomerDto);
  }

  @ApiOperation(value = "The API to get selected final customer for updating")
  @PostMapping("/{finalCustomerOrgId}/selected")
  @HasWholesalerPreAuthorization
  public UpdatingFinalCustomerDto getSelectedFinalCustomer(final OAuth2Authentication authed,
      @ApiParam @PathVariable Integer finalCustomerOrgId) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return finalCustomerService.getSelectedFinalCustomer(finalCustomerOrgId, user.getId(), user.isShowNetPriceEnabled());
  }

  @ApiOperation(value = "The API to update existing final customer")
  @PostMapping("/{finalCustomerOrgId}/update")
  @ResponseStatus(value = HttpStatus.OK)
  @HasWholesalerPreAuthorization
  public void updateFinalCustomer(@ApiParam @PathVariable Integer finalCustomerOrgId,
      @ApiParam @RequestBody SavingFinalCustomerDto finalCustomer) throws ServiceException {
    finalCustomerService.updateFinalCustomer(finalCustomerOrgId, finalCustomer);
  }

  @ApiOperation(value = "The API to delete existing final customer")
  @DeleteMapping("/{finalCustomerOrgId}/delete")
  @ResponseStatus(value = HttpStatus.OK)
  @HasWholesalerPreAuthorization
  public void deleteFinalCustomer(@ApiParam @PathVariable Integer finalCustomerOrgId)
      throws ServiceException {
    finalCustomerService.deleteFinalCustomer(finalCustomerOrgId);
  }
}
