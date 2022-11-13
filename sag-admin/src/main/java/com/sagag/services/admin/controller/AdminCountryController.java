package com.sagag.services.admin.controller;

import com.sagag.eshop.repo.entity.Country;
import com.sagag.eshop.service.api.CountryService;
import com.sagag.services.admin.swagger.docs.ApiDesc;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.utils.RestExceptionUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The controller exposes api for country.
 *
 */
@RestController
@RequestMapping(value = "/admin/country", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "admin")
public class AdminCountryController {

  @Autowired
  private CountryService countryService;

  @ApiOperation(value = ApiDesc.Country.GET_COUNTRY_SHORT_INFO_DESC,
      notes = ApiDesc.Country.GET_COUNTRY_SHORT_INFO_NOTE)
  @GetMapping(value = "/shortcode")
  public List<Country> getCountriesWithShortCode(final OAuth2Authentication authentication)
      throws ResultNotFoundException {
    return RestExceptionUtils
        .doSafelyReturnNotEmptyRecords(countryService.getCountriesWithShortCode());
  }

  @ApiOperation(value = ApiDesc.Country.GET_COUNTRY_INFO_DESC,
      notes = ApiDesc.Country.GET_COUNTRY_INFO_NOTE)
  @GetMapping(value = "/countries")
  public List<Country> getCountries(final OAuth2Authentication auth)
      throws ResultNotFoundException {
    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(countryService.getCountries());
  }

  @ApiOperation(value = ApiDesc.Country.GET_COUNTRY_INFO_BY_CODE_DESC,
      notes = ApiDesc.Country.GET_COUNTRY_INFO_BY_CODE_NOTE)
  @GetMapping(value = "/countries/{code}")
  public List<Country> getCountriesByCode(@PathVariable("code") final String code)
      throws ResultNotFoundException {
    return RestExceptionUtils
        .doSafelyReturnNotEmptyRecords(countryService.getCountriesByCode(code));
  }
}
