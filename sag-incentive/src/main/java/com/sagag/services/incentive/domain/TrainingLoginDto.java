package com.sagag.services.incentive.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class TrainingLoginDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String userId;

  private Long companyId;

  private String firstName;

  private String lastName;

  private String fs;
}
