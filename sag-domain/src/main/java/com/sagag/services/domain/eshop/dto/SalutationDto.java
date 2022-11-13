package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalutationDto implements Serializable {
  private static final long serialVersionUID = 2369736726021677553L;
  private int id;
  private String code;
  private String description;
}
