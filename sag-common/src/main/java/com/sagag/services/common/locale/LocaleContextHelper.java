package com.sagag.services.common.locale;

import com.google.common.collect.Sets;
import com.sagag.services.common.country.CountryConfiguration;
import com.sagag.services.common.enums.SupportedAffiliate;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

@Component
public class LocaleContextHelper {

  private static final String AT_COUNTRY_CODE = "at";

  private static final String CH_COUNTRY_CODE = "ch";

  private static final String EN_LANGUAGE_CODE = "en";

  @Autowired
  private CountryConfiguration countryConfig;

  public Locale toSupportedLocale(String lang) {
    return toLocaleByValue(StringUtils.lowerCase(lang), true);
  }

  public Locale toLocale(String lang) {
    return toLocaleByValue(StringUtils.lowerCase(lang), false);
  }

  public Locale toLocaleOrigin(String lang) {
    return toLocaleByValue(lang, false);
  }

  private Locale toLocaleByValue(String langVal, boolean filterSupportedLang) {
    if (StringUtils.isBlank(langVal)) {
      return defaultAppLocale();
    }
    try {
      final Predicate<String> filterSupportedLanguage = filterSupportedLang
        ? filterSupportedLanguage(countryConfig.getCode(), countryConfig.languages())
        : l -> true;
      return Optional.of(langVal).filter(filterSupportedLanguage)
          .map(LocaleUtils::toLocale).orElseGet(this::defaultAppLocale);
    } catch (IllegalArgumentException ex) {
      return defaultAppLocale();
    }
  }

  private static Predicate<String> filterSupportedLanguage(String countryCode,
    String[] supportedLanguages) {
    return language -> {
      final Set<String> languages = Sets.newHashSet(supportedLanguages);
      if (StringUtils.equalsIgnoreCase(AT_COUNTRY_CODE, countryCode)
        || StringUtils.equalsIgnoreCase(CH_COUNTRY_CODE, countryCode)) {
        languages.removeIf(l -> StringUtils.equalsIgnoreCase(l, EN_LANGUAGE_CODE));
      }
      return languages.stream().anyMatch(l -> StringUtils.equalsIgnoreCase(l, language));
    };
  }

  public Locale defaultLocale(Locale locale) {
    if (locale == null) {
      return defaultAppLocale();
    }
    return locale;
  }

  public Locale defaultAppLocale() {
    return new Locale(countryConfig.defaultLanguage(), countryConfig.getCode());
  }

  public String defaultAppLocaleLanguage() {
    return defaultAppLocale().getLanguage();
  }

  public String defaultAppLocaleCountry() {
    return defaultAppLocale().getCountry();
  }

  public String findCountryByAffiliate(Optional<SupportedAffiliate> affiliateOpt) {
    if (affiliateOpt.filter(SupportedAffiliate::isAtAffiliate).isPresent()) {
      return LocaleContextHelper.AT_COUNTRY_CODE;
    }
    return defaultAppLocaleCountry();
  }

}
