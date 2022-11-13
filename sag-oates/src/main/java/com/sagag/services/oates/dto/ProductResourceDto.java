package com.sagag.services.oates.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class ProductResourceDto implements Serializable {

  private static final long serialVersionUID = 819593860716441415L;

  private String type;

  private String uri;
}
