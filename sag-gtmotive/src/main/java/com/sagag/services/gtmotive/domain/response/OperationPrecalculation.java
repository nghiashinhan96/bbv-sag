package com.sagag.services.gtmotive.domain.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class OperationPrecalculation implements Serializable {

  private static final long serialVersionUID = 8129004083081392137L;
  private String code;
  private Double units;

  // other attributes will added as demand
}
