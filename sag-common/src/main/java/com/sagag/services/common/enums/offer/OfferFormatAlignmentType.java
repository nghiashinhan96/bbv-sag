package com.sagag.services.common.enums.offer;

import lombok.Getter;

@Getter
public enum OfferFormatAlignmentType {

  LEFT("left"),
  RIGHT("right");

  private String value;

  private OfferFormatAlignmentType(final String value) {
    this.value = value;
  }

}
