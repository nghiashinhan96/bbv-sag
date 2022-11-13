package com.sagag.services.thule.domain;

import java.util.Map;

import lombok.Data;

@Data
public class ThuleProperties {

  private String endpoint;

  private Map<String, String> dealerIdMap;
}
