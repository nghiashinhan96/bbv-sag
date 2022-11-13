package com.sagag.services.gtmotive.lang;

import com.sagag.services.common.profiles.AtSbProfile;

import org.springframework.stereotype.Component;

import java.util.Locale;

@AtSbProfile
@Component
public class AtGtmotiveLanguageProvider implements GtmotiveLanguageProvider {

  @Override
  public Locale getUserLang() {
    return Locale.GERMAN;
  }
}
