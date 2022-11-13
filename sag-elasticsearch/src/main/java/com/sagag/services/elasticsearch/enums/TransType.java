package com.sagag.services.elasticsearch.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransType {

  CH("typs_ch"),
  AT("typs_at"),
  KBANR("kbanr"),
  FR("typs_fr");

  private String code;

}
