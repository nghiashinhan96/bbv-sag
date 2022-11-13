package com.sagag.eshop.service.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * Permission's functions class.
 */
@Data
@Builder
public class FunctionDto implements Serializable {

  private static final long serialVersionUID = -4095282245059914635L;

  private int id;

  private String functionName;

  private String description;

  private String relativeUrl;

}
