package com.sagag.eshop.service.dto.offer;

import lombok.Data;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ReportGroupedOfferPositionDto implements Serializable {

  private static final long serialVersionUID = -5153191224368433543L;

  private String title;

  private List<ReportOfferPositionDto> reportOfferPositions;

  public ReportGroupedOfferPositionDto(final String title,
      final List<OfferPositionDto> offerPositions, NumberFormat numberFormat) {
    this.title = title;
    this.reportOfferPositions = offerPositions.stream()
        .map(item -> new ReportOfferPositionDto(item, numberFormat)).collect(Collectors.toList());
  }
}
