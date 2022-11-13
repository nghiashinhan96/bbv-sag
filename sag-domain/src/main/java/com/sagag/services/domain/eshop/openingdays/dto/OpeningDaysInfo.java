package com.sagag.services.domain.eshop.openingdays.dto;

import com.sagag.services.common.enums.WorkingDayCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpeningDaysInfo implements Serializable {

  private static final long serialVersionUID = 1572680957998934554L;

  private WorkingDayCode workingDayCode;

  private OpeningDaysExceptionInfo exceptions;
}