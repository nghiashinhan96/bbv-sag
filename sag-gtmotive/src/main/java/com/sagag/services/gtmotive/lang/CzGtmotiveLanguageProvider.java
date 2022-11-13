package com.sagag.services.gtmotive.lang;

import com.sagag.services.common.profiles.CzProfile;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@CzProfile
@Component
public class CzGtmotiveLanguageProvider implements GtmotiveLanguageProvider {

  @Override
  public Locale getUserLang() {
    return LocaleContextHolder.getLocale();
  }
}
