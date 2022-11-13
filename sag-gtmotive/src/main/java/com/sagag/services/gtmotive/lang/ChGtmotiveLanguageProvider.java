package com.sagag.services.gtmotive.lang;

import com.sagag.services.common.profiles.ChProfile;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@ChProfile
@Component
public class ChGtmotiveLanguageProvider implements GtmotiveLanguageProvider {

  @Override
  public Locale getUserLang() {
    return LocaleContextHolder.getLocale();
  }
}
