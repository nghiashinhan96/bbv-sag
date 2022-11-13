package com.sagag.services.incentive.api.impl;

import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.incentive.api.OutletService;
import com.sagag.services.incentive.builder.outlet.DerendingerChOutletBuilder;
import com.sagag.services.incentive.builder.outlet.TechnomagOutletBuilder;
import com.sagag.services.incentive.config.IncentiveProfile;
import com.sagag.services.incentive.config.IncentiveProperties;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@IncentiveProfile
@Slf4j
public class OutletServiceImpl implements OutletService {

  private static final String LOG =
      "Returning Outlet URL by affiliate = {}, lang = {}, email = {}, username = {}, custNr = {}";

  @Autowired
  private IncentiveProperties incentiveProps;

  @Override
  public String getOutletUrl(SupportedAffiliate affiliate, String lang, String email,
      String username, Long custNr) {
    log.debug(LOG, affiliate, lang, email, username, custNr);
    switch (affiliate) {
      case DERENDINGER_CH:
        return new DerendingerChOutletBuilder(incentiveProps.getDdchOutlet(), lang, email)
            .buildOutlet();
      case TECHNOMAG:
        return new TechnomagOutletBuilder(incentiveProps.getTechnoOutlet(), lang, email, username,
            custNr).buildOutlet();
      default:
        throw new UnsupportedOperationException("Not support for affiliate");
    }
  }

}
