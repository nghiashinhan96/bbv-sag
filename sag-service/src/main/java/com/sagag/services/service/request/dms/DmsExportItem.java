package com.sagag.services.service.request.dms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@Data
public class DmsExportItem implements Serializable {

  private static final long serialVersionUID = -5276898482907833689L;

  private String articleNumber;
  private String totalGrossPriceIncl;
  private String totalGrossPrice;
  private String grossPrice;
  private String description;
  private double quantity;
  // For version 3
  private String totalNetPriceInclVat;
  private String totalNetPrice;
  private String netPrice;
  private String totalUvpeIncl; // 10. total of Suggested Retail Price include vat
  private String totalUvpe; // 11. total of UVPE price
  private String uvpe; // 12. single UVPE price

}
