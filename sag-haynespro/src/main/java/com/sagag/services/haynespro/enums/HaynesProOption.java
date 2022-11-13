package com.sagag.services.haynespro.enums;

import com.sagag.services.haynespro.dto.HaynesProOptionDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HaynesProOption {

  MAINTENANCE("maintenance"),
  ADJUSTMENT("adjustment"),
  LUBRICANTS("lubricants"),
  DRAWINGS("drawings"),
  REPAIR_TIMES("repairtimes"),
  FUSES("fuses"),
  ELECTRONICS("electronics"),
  SMARTFIX("smartfix"),
  SMARTCASE("smartcase"),
  RECALLS("recalls"),
  DIAGNOSIS("diagnosis");

  private String code;

  public static HaynesProOptionDto toDto(HaynesProOption option) {
    return HaynesProOptionDto.of(option.getCode(), option.name());
  }

}
