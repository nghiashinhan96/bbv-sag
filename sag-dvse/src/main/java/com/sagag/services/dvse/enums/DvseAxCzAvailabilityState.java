package com.sagag.services.dvse.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DvseAxCzAvailabilityState {

  GREEN(2, "Available", "Skladem", "Unbekannt"),
  YELLOW(3, "Partially available", "Částečně dostupné", "Teilweise verfügbar"),
  RED(4, "Not available", "Nedostupné", "Nicht verfügbar"),
  UNKNOWN(1, "Unknown", "Neznámé", "Unbekannt");

  private int code;

  private String shortDescriptionEn;
  private String shortDescriptionCs;
  private String shortDescriptionDe;

  public static String translateAvailStateByLanguageCode(String languageCode,
      DvseAxCzAvailabilityState state) {
    switch (languageCode) {
      case "en":
        return state.getShortDescriptionEn();
      case "cs":
        return state.getShortDescriptionCs();
      case "de":
        return state.getShortDescriptionDe();
      default:
        return state.getShortDescriptionCs();
    }

  }

}
