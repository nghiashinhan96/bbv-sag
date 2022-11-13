package com.sagag.services.tools.domain.csv;

import java.io.Serializable;

import com.sagag.services.tools.support.CountryCode;
import com.sagag.services.tools.support.SupportedAffiliate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustInfo implements Serializable {

  private static final long serialVersionUID = -8809711288178249886L;

  private String customerNr;

  private SupportedAffiliate aff;

  public CustInfo(final String customerNr, final CountryCode countryCode) {
    this.customerNr = customerNr;
    this.aff = SupportedAffiliate.fromCustomerNr(customerNr, countryCode);
  }
}

