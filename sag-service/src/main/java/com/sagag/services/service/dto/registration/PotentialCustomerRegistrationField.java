package com.sagag.services.service.dto.registration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PotentialCustomerRegistrationField implements Serializable {

  private static final long serialVersionUID = -4659793261347618996L;

  private String key;
  private String value;
  private String title;
}
