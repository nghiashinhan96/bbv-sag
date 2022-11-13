package com.sagag.services.service.invoice;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.NoSuchElementException;

@Getter
@AllArgsConstructor
public enum InvoiceCompanyDb {

  DERENDINGER_CH("derendinger-ch", "P10"),
  TECHNOMAG("technomag", "PTM"),
  MATIK_CH("matik-ch", "PMC"),
  WBB("wbb", "PWB"),
  KLAUS("klaus", "PKL");

  private String affiliate;

  private String companyDb;

  public static InvoiceCompanyDb fromDesc(final String desc) {
    return Arrays.asList(values()).stream()
        .filter(val -> StringUtils.equals(desc, val.getAffiliate())).findFirst()
        .orElseThrow(() -> new NoSuchElementException("No support the given affiliate name"));
  }

}
