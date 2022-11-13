package com.sagag.services.rest.controller;

import com.sagag.eshop.service.api.LicenseService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.domain.eshop.dto.LicenseSettingsDto;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.rest.authorization.annotation.IsAccessibleUrlPreAuthorization;
import com.sagag.services.rest.swagger.docs.ApiDesc;
import com.sagag.services.service.api.VinPackageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * VIN controller service.
 */
@RestController
@RequestMapping("/vin")
@Api(tags = "VIN Package APIs")
public class VinPackageController {

  @Autowired
  private LicenseService licenseService;

  @Autowired
  private VinPackageService vinPackageService;

  /**
   * Adds vin package to shopping cart.
   *
   * @param packageId the package Number
   * @param authed the user who requests
   * @return the result of {@link ShoppingCart}
   * @throws ResultNotFoundException exception
   */
  //@formatter:off
  @ApiOperation(
      value = ApiDesc.Vin.ADD_LICENSE_PACKAGE_API_DESC,
      notes = ApiDesc.Vin.ADD_LICENSE_PACKAGE_API_NOTE
  )
  @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
  public ShoppingCart addVinToCart(final OAuth2Authentication authed,
      @RequestParam("packageNumber") final long packageId) throws ResultNotFoundException {
    //@formatter:on
    final LicenseSettingsDto vinLicense = licenseService.getLicenseSettingsByPackId(packageId)
        .orElseThrow(() -> new ResultNotFoundException("License package was not found."));
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return vinPackageService.addVinItemToShoppingCart(user, vinLicense);
  }

  /**
   * Gets vin calls left for logged in user.
   *
   * @param customerNr the user customer number
   * @return the total available vin call number
   */
  @ApiOperation(
      value = ApiDesc.Vin.GET_VIN_CALLS_LEFT_DESC,
      notes = ApiDesc.Vin.GET_VIN_CALLS_LEFT_NOTE
  )
  @GetMapping(value = "/calls", produces = MediaType.APPLICATION_JSON_VALUE)
  public int getAvailableVinCalls(@RequestParam("customerNr") final Long customerNr) {
    return vinPackageService.searchAvailableVinCalls(customerNr);
  }

  /**
   * Gets all available vin license packages.
   *
   * @return the list of {@link LicenseSettingsDto}
   */
  @ApiOperation(
      value = ApiDesc.Vin.GET_VIN_PACKAGES_DESC,
      notes = ApiDesc.Vin.GET_VIN_PACKAGES_NOTE
  )
  @IsAccessibleUrlPreAuthorization
  @GetMapping(value = "/packages", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<LicenseSettingsDto> getVinPackages(HttpServletRequest request) {
    return vinPackageService.searchVinPackages();
  }
}
