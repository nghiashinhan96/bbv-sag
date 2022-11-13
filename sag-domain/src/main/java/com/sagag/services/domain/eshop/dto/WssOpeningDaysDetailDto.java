package com.sagag.services.domain.eshop.dto;

import com.sagag.services.domain.eshop.openingdays.dto.WssOpeningDaysExceptionDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WssOpeningDaysDetailDto implements Serializable {

  private static final long serialVersionUID = -175779640420282671L;

  private Integer id;

  private Date date;

  private Integer countryId;

  private String workingDayCode;

  private WssOpeningDaysExceptionDto exceptions;

}
