package com.sagag.services.domain.sag.erp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErpArticlePrice implements Serializable {

  private static final long serialVersionUID = 1350876427169074230L;

  private int type;

  private String description;

  private Double value;

  private Double rebateValue;

  private Double rebate;

  private String currencySymbol;

  private String currencyCode;

  private Double priceUnit;

  private Double vat;

  private boolean taxIncluded;
}
