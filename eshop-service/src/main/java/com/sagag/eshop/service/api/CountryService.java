package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.entity.Country;
import com.sagag.services.domain.eshop.dto.externalvendor.CountryDto;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Interface to define services for country.
 */
public interface CountryService {

  /**
   * Gets country list with short code
   *
   * @return the list of {@link Country}
   */
  List<Country> getCountriesWithShortCode();

  /**
   * Gets all countries
   *
   * @return the list of {@link Country}
   */
  List<Country> getCountries();

  /**
   * Gets all countries by code
   *
   * @param code country short code
   *
   * @return the list of {@link Country}
   */
  List<Country> getCountriesByCode(String code);

  /**
   * Returns the supported countries.
   *
   * @return the result list
   */
  List<CountryDto> getSupportedCountries();

  /**
   * Returns the optional country of country.
   *
   * @param affiliate the search affiliate
   * @return the optional result
   */
  Optional<String> searchCountryCodeByAffiliate(String affiliate);

  /**
   * Returns the default locale of affiliate.
   *
   * @param affiliate the search affiliate
   * @return the result locale
   */
  Locale searchDefaultLocaleByAffiliate(String affiliate);
}
