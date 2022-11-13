package com.sagag.services.gtmotive.app;

import com.sagag.services.gtmotive.domain.GtmotiveAccounts;

import lombok.Data;

@Data
public class GtAccountsConfiguration {

  private String url;
  private boolean multilang;
  private GtmotiveAccounts accounts;
}
