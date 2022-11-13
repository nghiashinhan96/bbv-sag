package com.sagag.eshop.service.dto;

import com.sagag.eshop.repo.entity.OpeningDaysCalendar;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.eshop.openingdays.dto.OpeningDaysExceptionDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpeningDaysDetailDto implements Serializable {

  private static final long serialVersionUID = -8809711288178249886L;

  private Integer id;

  private Date date;

  private Integer countryId;

  private String workingDayCode;

  private OpeningDaysExceptionDto exceptions;

  public OpeningDaysDetailDto(final OpeningDaysCalendar entity) {
    this.id = entity.getId();
    this.date = entity.getDatetime();
    this.countryId = entity.getCountry().getId();
    this.workingDayCode = entity.getWorkingDay().getCode();
    if (StringUtils.isBlank(entity.getExceptions())) {
      return;
    }
    this.exceptions =
        SagJSONUtil.convertJsonToObject(entity.getExceptions(), OpeningDaysExceptionDto.class);
  }
}
