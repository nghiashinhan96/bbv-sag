package com.sagag.services.gtmotive.dto.response.gtinterface;

import lombok.Data;

import java.io.Serializable;

@Data
public class GtmotivePartInfoOperation implements Serializable {

  private static final long serialVersionUID = 1L;

  private String shortNumber;
  private String description;
  private String partNumber;
  private String reference;
  private boolean isMultiReference;
}
