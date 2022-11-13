package com.sagag.services.common.enums;

import lombok.Getter;

@Getter
public enum LanguageCode {

  EN("en"),
  DE("de"),
  FR("fr"),
  IT("it"),
  CS("cs"),
  SR("sr");

  private String code;

  LanguageCode(String code) {
    this.code = code;
  }

}
