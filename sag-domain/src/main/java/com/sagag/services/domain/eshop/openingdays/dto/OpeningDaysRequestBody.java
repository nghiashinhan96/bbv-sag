package com.sagag.services.domain.eshop.openingdays.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpeningDaysRequestBody implements Serializable {

  private static final long serialVersionUID = -8809711288178249880L;

  private Integer id;

  @NotNull(message = "The date must not be null")
  private String date;

  @NotNull(message = "Country id must not be null")
  private Integer countryId;

  @NotNull(message = "Working day code must not be null")
  private String workingDayCode;

  private OpeningDaysExceptionDto exceptions;
}
