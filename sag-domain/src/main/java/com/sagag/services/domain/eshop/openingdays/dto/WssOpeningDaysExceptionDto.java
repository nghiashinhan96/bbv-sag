package com.sagag.services.domain.eshop.openingdays.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WssOpeningDaysExceptionDto implements Serializable {

  private static final long serialVersionUID = 2721273642339492041L;

  private List<String> branches;

  private String workingDayCode;


}
