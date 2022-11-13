package com.sagag.services.gtmotive.lang;

import com.sagag.services.common.profiles.AxCzProfile;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@AxCzProfile
@Component
public class AxCzGtmotiveLanguageProvider implements GtmotiveLanguageProvider {

  @Override
  public Locale getUserLang() {
    return LocaleContextHolder.getLocale();
  }
}
