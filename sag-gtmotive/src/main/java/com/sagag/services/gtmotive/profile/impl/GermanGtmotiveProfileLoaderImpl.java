package com.sagag.services.gtmotive.profile.impl;

import com.sagag.services.gtmotive.config.GtmotiveProfile;
import com.sagag.services.gtmotive.domain.GtmotiveAccounts;
import com.sagag.services.gtmotive.domain.GtmotiveProfileDto;
import com.sagag.services.gtmotive.profile.GtmotiveProfileLoader;

import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@GtmotiveProfile
public class GermanGtmotiveProfileLoaderImpl implements GtmotiveProfileLoader {

  @Override
  public Locale locale() {
    return Locale.GERMAN;
  }

  @Override
  public GtmotiveProfileDto getProfile(GtmotiveAccounts accounts) {
    return accounts.getDe();
  }

  @Override
  public String getSupplyTypeNew() {
    return "Neu";
  }

  @Override
  public String getSupplyTypeStandard() {
    return "Standard";
  }

  @Override
  public String getSupplyTypeEconomic() {
    return "Economic";
  }

}
