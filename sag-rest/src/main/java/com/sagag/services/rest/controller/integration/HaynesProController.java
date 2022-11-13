package com.sagag.services.rest.controller.integration;

import com.sagag.eshop.service.api.LicenseService;
import com.sagag.eshop.service.dto.HaynesProLicenseSettingDto;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.formatter.NumberFormatterContext;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.domain.sag.haynespro.LabourTimeJobDto;
import com.sagag.services.haynespro.api.HaynesProService;
import com.sagag.services.haynespro.config.HaynesProInternalProperties;
import com.sagag.services.haynespro.config.HaynesProProfile;
import com.sagag.services.haynespro.dto.HaynesProOptionDto;
import com.sagag.services.haynespro.request.HaynesProAccessUrlRequest;
import com.sagag.services.hazelcast.api.HaynesProCacheService;
import com.sagag.services.ivds.api.IvdsHaynesProSearchService;
import com.sagag.services.ivds.response.HaynesProClientResponse;
import com.sagag.services.rest.authorization.annotation.IsAccessibleUrlPreAuthorization;
import com.sagag.services.rest.resource.HaynesProAccessUrlResponse;
import com.sagag.services.rest.swagger.docs.ApiDesc;
import com.sagag.services.service.mail.haynespro.RequestTrialHaynesProLicenseMailSender;
import com.sagag.services.service.mail.haynespro.RequestTrialLicenseHaynesProCriteria;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

/**
 * Controller class for HaynesPro APIs.
 */
@RestController
@RequestMapping(value = "/haynespro", produces = MediaType.APPLICATION_JSON_VALUE)
@HaynesProProfile
@Api(tags = "HaynesPro Integration APIs")
public class HaynesProController {

  @Autowired
  private LicenseService licenseService;

  @Autowired
  private IvdsHaynesProSearchService ivdsHaynesProSearchService;

  @Autowired
  private HaynesProService haynesProService;

  @Autowired
  private HaynesProCacheService haynesProCacheService;

  @Autowired
  private RequestTrialHaynesProLicenseMailSender requestTrialHaynesProLicenseMailSender;

  @Autowired
  private HaynesProInternalProperties hpInternalProps;

  @Autowired
  private NumberFormatterContext numberFormatter;

  @GetMapping("/access/options")
  @IsAccessibleUrlPreAuthorization
  public List<HaynesProOptionDto> getHpAccessOptions(final HttpServletRequest request)
      throws ResultNotFoundException {
    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(
        haynesProService.getHaynesProAccessOptions());
  }

  @ApiOperation(
    value = ApiDesc.HaynesPro.GET_HAYNESPRO_API_DESC,
    notes = ApiDesc.HaynesPro.GET_HAYNESPRO_API_NOTE)
  @PostMapping("/access/url")
  public HaynesProAccessUrlResponse getHaynesProAccessUrl(final OAuth2Authentication authed,
      @RequestBody final HaynesProAccessUrlRequest requestBody) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    requestBody.setUuid(user.key());
    requestBody.setLanguage(user.getLanguage());
    requestBody.setUsername(user.getCachedUsername());
    final NumberFormat numberFormat =
        numberFormatter.getFormatterByAffiliateShortName(user.getCollectionShortname());
    String currencyCode =
        user.getSupportedAffiliate().isPdpAffiliate() ? user.getCustomer().getCurrency()
            : numberFormat.getCurrency().getCurrencyCode();
    requestBody.setCurrencyCode(currencyCode);
    return HaynesProAccessUrlResponse.of(haynesProService.getHaynesProAccessUrl(requestBody));
  }

  @ApiOperation(
    value = ApiDesc.HaynesPro.GET_HAYNESPRO_API_DESC,
    notes = ApiDesc.HaynesPro.GET_HAYNESPRO_API_NOTE)
  @PostMapping(value = "/callback", consumes = MediaType.ALL_VALUE)
  public void callback(final HttpServletRequest request,
      @RequestParam(name = "uuid", defaultValue = "") final String uuid,
      @RequestParam(name = "vehId", defaultValue = "") final String vehId) throws IOException {
    ivdsHaynesProSearchService.handleHaynesProCallback(uuid, vehId, request.getReader());
  }

  @ApiOperation(
    value = ApiDesc.HaynesPro.GET_HAYNESPRO_DISPLAY_SMART_CART_RESPONSE_TO_PART_LIST_DESC,
    notes = ApiDesc.HaynesPro.GET_HAYNESPRO_DISPLAY_SMART_CART_RESPONSE_TO_PART_LIST_NOTE)
  @GetMapping("/response")
  public HaynesProClientResponse getClientResponse(final OAuth2Authentication authed,
      @RequestParam(value = "vehicleId", required = false) final String vehicleId)
      throws ServiceException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return ivdsHaynesProSearchService.getHaynesProResponse(user.key(), vehicleId);
  }

  @ApiOperation(
    value = ApiDesc.HaynesPro.GET_HAYNESPRO_LICENSE_API_DESC,
    notes = ApiDesc.HaynesPro.GET_HAYNESPRO_LICENSE_API_NOTE)
  @GetMapping("/license")
  @IsAccessibleUrlPreAuthorization
  public HaynesProLicenseSettingDto getHaynesProLicense(final HttpServletRequest request,
      final OAuth2Authentication authed) throws ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return RestExceptionUtils.doSafelyReturnOptionalRecord(
        licenseService.getHaynesProLicense(user.getCustNrStr()));
  }

  @ApiOperation(
      value = ApiDesc.HaynesPro.REQUEST_TRIAL_HAYNESPRO_LICENSE_API_DESC,
      notes = ApiDesc.HaynesPro.REQUEST_TRIAL_HAYNESPRO_LICENSE_API_NOTE)
  @GetMapping(value = "/license/request", produces = MediaType.APPLICATION_JSON_VALUE)
  public void requestTrialLicense(final OAuth2Authentication authed,
      @RequestParam(value = "type", defaultValue = "trial") final String requestType)
          throws MessagingException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    final String receiptEmail = hpInternalProps.getLicense()
        .getEmailByAffiliate(user.getSupportedAffiliate());

    Assert.hasText(receiptEmail, "Not found receipt email to send request email in configuration");

    final RequestTrialLicenseHaynesProCriteria criteria =
        RequestTrialLicenseHaynesProCriteria.builder()
        .affiliateEmail(user.getSettings().getAffiliateEmail())
        .receiptEmail(receiptEmail)
        .customerNr(user.getCustNrStr())
        .username(user.getUsername())
        .email(user.getEmail())
        .locale(user.getUserLocale())
        .requestType(requestType).build();

    // Send request trial HaynesPro license mail
    requestTrialHaynesProLicenseMailSender.send(criteria);
  }

  @ApiOperation(
      value = ApiDesc.HaynesPro.GET_HAYNESPRO_LABOUR_TIME_DESC,
      notes = ApiDesc.HaynesPro.GET_HAYNESPRO_LABOUR_TIME_NOTE)
  @GetMapping(value = "/labour-time")
  @IsAccessibleUrlPreAuthorization
  public List<LabourTimeJobDto> getLabourTime(final HttpServletRequest request,
      final OAuth2Authentication authed,
      @RequestParam("vehicleId") String vehicleId) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return haynesProCacheService.getLabourTimes(user.key(), vehicleId,
        user.getSettings().getVatRate());
  }

  @ApiOperation(
      value = ApiDesc.HaynesPro.REMOVE_HAYNESPRO_LABOUR_TIME_DESC,
      notes = ApiDesc.HaynesPro.REMOVE_HAYNESPRO_LABOUR_TIME_NOTE)
  @PutMapping(value = "/labour-time/remove")
  @ResponseStatus(HttpStatus.OK)
  public void removeLabourTime(final HttpServletRequest request,
      final OAuth2Authentication authed,
      @RequestParam("vehicleId") String vehicleId,
      @RequestParam(value = "awNumber", defaultValue = "") String awNumber) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    haynesProCacheService.removeLabourTime(user.key(), vehicleId, awNumber);
  }

}
