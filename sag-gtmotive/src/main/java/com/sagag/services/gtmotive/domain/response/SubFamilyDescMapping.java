package com.sagag.services.gtmotive.domain.response;

import com.google.common.base.Enums;
import com.google.common.base.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
public enum SubFamilyDescMapping {
  DE("ANTRIEB"),
  FR("TRANSMISSION"),
  IT("TRASMISSIONE"),
  CS("POHON"),
  EN("TRANSMISSION"),
  RO("TRANSMISIE"),
  HU("ERŐÁTVITEL");

  private String subFamilyDescription;

  public static String getDescFromCode(String langIsoCode) {
    if (StringUtils.isEmpty(langIsoCode)) {
      return SubFamilyDescMapping.DE.getSubFamilyDescription();
    }
    Optional<SubFamilyDescMapping> ifPresent =
        Enums.getIfPresent(SubFamilyDescMapping.class, langIsoCode.toUpperCase());
    if (ifPresent.isPresent()) {
      return ifPresent.get().getSubFamilyDescription();
    }
    throw new IllegalArgumentException(
        "SubFamilyDesc not suppoted for this language iso code: " + langIsoCode);
  }

}
