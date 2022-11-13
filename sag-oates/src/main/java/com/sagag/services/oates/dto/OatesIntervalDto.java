package com.sagag.services.oates.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class OatesIntervalDto implements Serializable {

  private static final long serialVersionUID = -1782243106586313613L;

  private String displayName;

  private String displayUnit;

  private String unit;

  private String text;

}
