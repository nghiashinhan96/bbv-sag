package com.sagag.services.tools.domain.target;

import lombok.Data;

import java.io.Serializable;

@Data
public class SalutationDto implements Serializable {
  private static final long serialVersionUID = 2369736726021677553L;
  private int id;
  private String code;
  private String description;
}
