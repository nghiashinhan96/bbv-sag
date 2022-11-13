package com.sagag.services.gtmotive.domain.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class GtmotiveSimpleAttribute implements Serializable {

  private static final long serialVersionUID = -2176321953760050436L;

  private String id;
  private String value;
}
