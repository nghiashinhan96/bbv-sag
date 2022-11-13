package com.sagag.services.gtmotive.profile.impl;

import com.sagag.services.gtmotive.config.GtmotiveProfile;
import com.sagag.services.gtmotive.domain.GtmotiveAccounts;
import com.sagag.services.gtmotive.domain.GtmotiveProfileDto;
import com.sagag.services.gtmotive.profile.GtmotiveProfileLoader;

import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@GtmotiveProfile
public class ItalianGtmotiveProfileLoaderImpl implements GtmotiveProfileLoader {

  @Override
  public Locale locale() {
    return Locale.ITALIAN;
  }

  @Override
  public GtmotiveProfileDto getProfile(GtmotiveAccounts accounts) {
    return accounts.getIt();
  }

  @Override
  public String getSupplyTypeNew() {
    return "Nuova";
  }

  @Override
  public String getSupplyTypeStandard() {
    return "STANDARD";
  }

  @Override
  public String getSupplyTypeEconomic() {
    return "Economic";
  }

}
