package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.CountryRepository;
import com.sagag.eshop.repo.entity.Country;
import com.sagag.eshop.service.api.CountryService;
import com.sagag.services.common.country.CountryConfiguration;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.domain.eshop.dto.externalvendor.CountryDto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation class for country service.
 */
@Service
public class CountryServiceImpl implements CountryService {

  @Autowired
  private CountryRepository countryRepository;

  @Autowired
  private CountryConfiguration countryConfig;

  @Autowired
  private LocaleContextHelper localeContextHelper;

  @Override
  public List<Country> getCountriesWithShortCode() {
    return countryRepository.findAllWithShortCode();
  }

  @Override
  public List<Country> getCountries() {
    return countryRepository.findAll();
  }

  @Override
  public List<Country> getCountriesByCode(final String code) {
    return countryRepository.findByShortCode(code);
  }

  @Override
  public List<CountryDto> getSupportedCountries() {
    return countryRepository.findByCodeIn(
        Arrays.asList(countryConfig.getSupportedIso3Countries())).stream()
        .map(country -> CountryDto.builder().code(country.getShortCode())
            .description(country.getFullName()).build()).collect(Collectors.toList());
  }

  @Override
  public Optional<String> searchCountryCodeByAffiliate(String affiliate) {
    if (StringUtils.isBlank(affiliate)) {
      return Optional.empty();
    }
    return countryRepository.findShortCodeByAffiliate(affiliate);
  }

  @Override
  public Locale searchDefaultLocaleByAffiliate(String affiliate) {
    final String countryCode = searchCountryCodeByAffiliate(affiliate)
        .orElse(localeContextHelper.defaultAppLocaleCountry());
    return new Locale.Builder().setRegion(countryCode).build();
  }
}
