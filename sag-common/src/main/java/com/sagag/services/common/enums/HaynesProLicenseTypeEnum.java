package com.sagag.services.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

@Getter
@AllArgsConstructor
public enum HaynesProLicenseTypeEnum {

  ULTIMATE("carset", "ULTIMATE"),
  PROFESSIONAL("pro", "PREMIUM"),
  CAR_TECH_ELEC("cartechelec", "CarTech + Electronics");

  private String code;

  private String packageName;

  public static Optional<HaynesProLicenseTypeEnum> fromPackageName(String packageName) {
    if (StringUtils.isBlank(packageName)) {
      return Optional.empty();
    }
    for (HaynesProLicenseTypeEnum hpLicenseType : HaynesProLicenseTypeEnum.values()) {
      final int indexOfUltimateTxt =
          StringUtils.indexOf(StringUtils.upperCase(packageName),
              StringUtils.upperCase(hpLicenseType.getPackageName()));

      if (indexOfUltimateTxt != StringUtils.INDEX_NOT_FOUND) {
        return Optional.of(hpLicenseType);
      }
    }
    return Optional.of(PROFESSIONAL);
  }
}
