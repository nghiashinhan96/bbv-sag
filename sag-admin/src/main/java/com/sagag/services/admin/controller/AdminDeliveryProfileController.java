package com.sagag.services.admin.controller;

import com.sagag.eshop.repo.api.DeliveryProfileRepository;
import com.sagag.eshop.repo.entity.DeliveryProfile;
import com.sagag.eshop.repo.entity.VDeliveryProfile;
import com.sagag.eshop.service.api.DeliveryProfileService;
import com.sagag.eshop.service.dto.CsvDeliveryProfileDto;
import com.sagag.eshop.service.exception.DeliveryProfileValidationException;
import com.sagag.eshop.service.exception.DeliveryProfileValidationException.DeliveryProfileErrorCase;
import com.sagag.services.admin.swagger.docs.ApiDesc;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.exception.CsvServiceException;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.utils.CsvUtils;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.domain.eshop.criteria.DeliveryProfileSearchCriteria;
import com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileDto;
import com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileSavingDto;
import com.sagag.services.domain.eshop.dto.externalvendor.SupportDeliveryProfileDto;
import com.sagag.services.service.api.DeliveryProfileBusinessService;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin/delivery-profile")
@Api(tags = "Admin Delivery Profile Management APIs")
public class AdminDeliveryProfileController {

  @Autowired
  private DeliveryProfileBusinessService deliveryProfileBusService;

  @Autowired
  private DeliveryProfileService deliveryProfileService;

  @Autowired
  private DeliveryProfileRepository deliveryProfileRepo;

  @ApiOperation(value = ApiDesc.AdminDeliveryProfile.IMPORT_DELIVERY_PROFILE,
      notes = ApiDesc.AdminDeliveryProfile.IMPORT_DELIVERY_PROFILE_NOTE)
  @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public void importDeliveryProfile(@RequestParam("file") MultipartFile file)
      throws DeliveryProfileValidationException, CsvServiceException {
    final boolean useDefaultCharset = false;
    final List<CsvDeliveryProfileDto> csvDeliveryProfiles =
        CsvUtils.read(file, CsvDeliveryProfileDto.class, SagConstants.CSV_DEFAULT_SEPARATOR, useDefaultCharset);
    deliveryProfileBusService.importAndRefreshCacheDeliveryProfile(csvDeliveryProfiles);
  }


  @ApiOperation(value = ApiDesc.AdminDeliveryProfile.CREATE_DELIVERY_PROFILE,
      notes = ApiDesc.AdminDeliveryProfile.CREATE_DELIVERY_PROFILE_NOTE)
  @PostMapping("/create")
  @ResponseStatus(HttpStatus.OK)
  public void createDeliveryProfile(final OAuth2Authentication authed,
      @RequestBody final DeliveryProfileSavingDto profile)
      throws DeliveryProfileValidationException {
    final Long userId = Long.parseLong(authed.getPrincipal().toString());
    deliveryProfileService.createDeliveryProfile(profile, userId);
    deliveryProfileBusService.refreshCacheDeliveryProfile();
  }

  @ApiOperation(value = ApiDesc.AdminDeliveryProfile.EDIT_DELIVERY_PROFILE,
      notes = ApiDesc.AdminDeliveryProfile.EDIT_DELIVERY_PROFILE_NOTE)
  @PutMapping("/{deliveryProfileId}/update")
  @ResponseStatus(HttpStatus.OK)
  public void updateDeliveryProfile(final OAuth2Authentication authed,
      @PathVariable final Integer deliveryProfileId,
      @RequestBody final DeliveryProfileSavingDto profile)
      throws DeliveryProfileValidationException {
    final Long userId = Long.parseLong(authed.getPrincipal().toString());
    profile.setId(deliveryProfileId);
    deliveryProfileService.updateDeliveryProfile(profile, userId);
    deliveryProfileBusService.refreshCacheDeliveryProfile();
  }

  @ApiOperation(value = ApiDesc.AdminDeliveryProfile.DELETE_DELIVERY_PROFILE,
      notes = ApiDesc.AdminDeliveryProfile.DELETE_DELIVERY_PROFILE_NOTE)
  @DeleteMapping("/{deliveryProfileId}/remove")
  @ResponseStatus(HttpStatus.OK)
  public void removeDeliveryProfile(@PathVariable final Integer deliveryProfileId)
      throws DeliveryProfileValidationException {
    deliveryProfileService.removeDeliveryProfile(deliveryProfileId);
  }

  @PostMapping("/refresh")
  @ResponseStatus(HttpStatus.OK)
  public void refreshCacheDeliveryProfole() {
    deliveryProfileBusService.refreshCacheDeliveryProfile();
  }

  @ApiOperation(value = ApiDesc.AdminDeliveryProfile.SEARCH_DELIVERY_PROFILE,
      notes = ApiDesc.AdminDeliveryProfile.SEARCH_DELIVERY_PROFILE_NOTE)
  @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public Page<VDeliveryProfile> searchDeliveryProfile(
      @RequestBody final DeliveryProfileSearchCriteria searchCriteria)
      throws ResultNotFoundException {
    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(
        deliveryProfileService.searchDeliveryProfile(searchCriteria));
  }

  @ApiOperation(value = ApiDesc.AdminDeliveryProfile.CREATE_MASTER_DATA_DELIVERY_PROFILE,
      notes = ApiDesc.AdminDeliveryProfile.CREATE_MASTER_DATA_DELIVERY_PROFILE_NOTE)
  @GetMapping(value = "/init-data", produces = MediaType.APPLICATION_JSON_VALUE)
  public SupportDeliveryProfileDto getMasterDataDeliveryProfile() {
    return deliveryProfileService.getMasterDataDeliveryProfile();
  }

  @ApiOperation(value = ApiDesc.AdminDeliveryProfile.FIND_DELIVERY_PROFILE,
      notes = ApiDesc.AdminDeliveryProfile.FIND_DELIVERY_PROFILE_NOTE)
  @GetMapping(value = "/{id}/find", produces = MediaType.APPLICATION_JSON_VALUE)
  public DeliveryProfile findDeliveryProfileById(@PathVariable final Integer id)
      throws DeliveryProfileValidationException {
    return deliveryProfileRepo.findById(id).orElseThrow(
        () -> new DeliveryProfileValidationException(DeliveryProfileErrorCase.ODE_EMP_003,
            String.format("The delivery profile %d cannot find ", id)));
  }

  @ApiOperation(value = ApiDesc.AdminDeliveryProfile.FIND_DELIVERY_PROFILE_BY_DELIVERY_PROFILE_ID,
      notes = ApiDesc.AdminDeliveryProfile.FIND_DELIVERY_PROFILE_BY_DELIVERY_PROFILE_ID_NOTE)
  @GetMapping(value = "/{deliveryProfileId}/delivery-profile-name", produces = MediaType.APPLICATION_JSON_VALUE)
  public DeliveryProfileDto findDeliveryProfileNameByDeliveryProfileId(
      @PathVariable final Integer deliveryProfileId) {
    return deliveryProfileService.findProfileNameByDeliveryProfileId(deliveryProfileId);
  }
}
