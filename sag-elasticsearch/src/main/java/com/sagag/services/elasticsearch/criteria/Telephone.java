package com.sagag.services.elasticsearch.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * Telephone pattern class.
 */
@Data
@AllArgsConstructor
public class Telephone implements Serializable {

  private static final long serialVersionUID = 7261094419563751944L;

  private String countryCode;
  private final String number;
  
}
