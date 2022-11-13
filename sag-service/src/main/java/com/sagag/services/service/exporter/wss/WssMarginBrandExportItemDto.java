package com.sagag.services.service.exporter.wss;

import com.sagag.eshop.repo.entity.WssMarginsBrand;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WssMarginBrandExportItemDto {
  private Integer brandId;
  private String brandName;
  private Double margin1;
  private Double margin2;
  private Double margin3;
  private Double margin4;
  private Double margin5;
  private Double margin6;
  private Double margin7;

  public WssMarginBrandExportItemDto(WssMarginsBrand marginsBrand) {
    super();
    this.brandId = marginsBrand.getBrandId();
    this.brandName = marginsBrand.getBrandName();
    this.margin1 = marginsBrand.getMargin1();
    this.margin2 = marginsBrand.getMargin2();
    this.margin3 = marginsBrand.getMargin3();
    this.margin4 = marginsBrand.getMargin4();
    this.margin5 = marginsBrand.getMargin5();
    this.margin6 = marginsBrand.getMargin6();
    this.margin7 = marginsBrand.getMargin7();

  }
}
