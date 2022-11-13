package com.sagag.services.gtmotive.profile;

import com.sagag.services.gtmotive.domain.GtmotiveAccounts;
import com.sagag.services.gtmotive.domain.GtmotiveProfileDto;

import java.util.Locale;

public interface GtmotiveProfileLoader {

  Locale locale();

  GtmotiveProfileDto getProfile(GtmotiveAccounts accounts);

  String getSupplyTypeNew();

  String getSupplyTypeStandard();

  String getSupplyTypeEconomic();

}
