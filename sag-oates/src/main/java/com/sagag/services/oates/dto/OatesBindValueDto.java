package com.sagag.services.oates.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class OatesBindValueDto implements Serializable {

  private static final long serialVersionUID = 2175351419717394332L;

  private String displayName;

  private String displayValue;

  private String value;

}
