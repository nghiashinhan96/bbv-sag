package com.sagag.services.service.exporter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
public class ReportPriceItemDto implements Serializable {

  private static final long serialVersionUID = 7611821393751624363L;

  private String label;
  private String price;
  private String brand;

}
