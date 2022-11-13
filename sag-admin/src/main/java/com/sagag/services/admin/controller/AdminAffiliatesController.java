package com.sagag.services.admin.controller;

import com.sagag.eshop.service.api.AffiliateService;
import com.sagag.eshop.service.api.AffiliateSettingsService;
import com.sagag.eshop.service.exception.OrganisationCollectionException;
import com.sagag.services.admin.swagger.docs.ApiDesc;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.domain.eshop.dto.AvailabilitySettingMasterDataDto;
import com.sagag.services.domain.eshop.dto.BackOfficeAffiliateInfoDto;
import com.sagag.services.domain.eshop.dto.BackOfficeAffiliateSettingDto;
import com.sagag.services.domain.eshop.dto.BackOfficeAffiliateShortInfoDto;
import com.sagag.services.domain.eshop.dto.BoAffiliateSettingRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "affiliates")
@RequestMapping(value = "/admin/affiliates", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminAffiliatesController {

  @Autowired
  private AffiliateService affiliateService;

  @Autowired
  private AffiliateSettingsService affiliateSettingsService;

  @ApiOperation(value = ApiDesc.Affiliate.GET_AFFILIATE_SHORT_INFO_DESC,
      notes = ApiDesc.Affiliate.GET_AFFILIATE_SHORT_INFO_NOTE)
  @GetMapping(value = "/short-infos")
  public List<BackOfficeAffiliateShortInfoDto> getAllAffiliates(
      final OAuth2Authentication authentication) {
    return affiliateService.getShortInfos();
  }

  @ApiOperation(value = ApiDesc.Affiliate.GET_AFFILIATE_INFO_DESC,
      notes = ApiDesc.Affiliate.GET_AFFILIATE_INFO_NOTE)
  @GetMapping(value = "/infos")
  public List<BackOfficeAffiliateInfoDto> getAffiliateInfos(
      final OAuth2Authentication authentication,
      @RequestParam(value = "affShortName") String affShortName) {
    return affiliateService.getInfos(affShortName);
  }

  @ApiOperation(value = ApiDesc.Affiliate.GET_AFFILIATE_SETTINGS_DESC,
      notes = ApiDesc.Affiliate.GET_AFFILIATE_SETTINGS_NOTE)
  @GetMapping(value = "/settings")
  public BackOfficeAffiliateSettingDto getSetting(
      @RequestParam(value = "affShortName") String affShortName) {
    return affiliateService.getSettings(affShortName);
  }

  @ApiOperation(value = ApiDesc.Affiliate.UPDATE_AFFILIATE_SETTINGS_DESC,
      notes = ApiDesc.Affiliate.UPDATE_AFFILIATE_SETTINGS_NOTE)
  @PostMapping(value = "/settings/update")
  @ResponseStatus(value = HttpStatus.OK)
  public void updateSetting(@RequestBody final BoAffiliateSettingRequest requestBody)
      throws OrganisationCollectionException {
    affiliateSettingsService.updateSettings(requestBody);
  }

  @ApiOperation(value = ApiDesc.Affiliate.GET_AFFILIATE_SHORT_INFO_BY_COUNTRY_DESC,
      notes = ApiDesc.Affiliate.GET_AFFILIATE_SHORT_INFO_BY_COUNTRY_NOTE)
  @GetMapping(value = "/short-infos/{countryShortCode}")
  public List<BackOfficeAffiliateShortInfoDto> getAffiliateInfosByCountry(
      @PathVariable("countryShortCode") final String countryShortCode)
      throws ResultNotFoundException {
    return RestExceptionUtils
        .doSafelyReturnNotEmptyRecords(affiliateService.getShortInfosByCountry(countryShortCode));
  }
  
  @ApiOperation(value = ApiDesc.Affiliate.GET_AVAILABILITY_SETTING_MASTER_DATA_DESC,
      notes = ApiDesc.Affiliate.GET_AVAILABILITY_SETTING_MASTER_DATA)
  @GetMapping(value = "/settings/availability/master-data", produces = MediaType.APPLICATION_JSON_VALUE)
  public AvailabilitySettingMasterDataDto getMasterData() {
    return affiliateSettingsService.getAvailabilitySettingMasterData();
  }
}
