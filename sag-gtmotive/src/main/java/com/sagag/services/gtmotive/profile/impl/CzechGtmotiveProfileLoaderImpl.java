package com.sagag.services.gtmotive.profile.impl;

import com.sagag.services.common.enums.LanguageCode;
import com.sagag.services.gtmotive.config.GtmotiveProfile;
import com.sagag.services.gtmotive.domain.GtmotiveAccounts;
import com.sagag.services.gtmotive.domain.GtmotiveProfileDto;
import com.sagag.services.gtmotive.profile.GtmotiveProfileLoader;
import java.util.Locale;
import org.springframework.stereotype.Component;

@Component
@GtmotiveProfile
public class CzechGtmotiveProfileLoaderImpl implements GtmotiveProfileLoader {

  @Override
  public Locale locale() {
    return new Locale(LanguageCode.CS.getCode());
  }

  @Override
  public GtmotiveProfileDto getProfile(GtmotiveAccounts accounts) {
    return accounts.getCs();
  }

  @Override
  public String getSupplyTypeNew() {
    return "Nový ND";
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
