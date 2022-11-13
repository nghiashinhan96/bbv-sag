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
public class OpeningDaysExceptionDto implements Serializable {

  private static final long serialVersionUID = -8809711288178249886L;

  // Affiliate company name. Ex: Derendinger-Austria
  private String affiliate;

  // List branch number
  private List<String> branches;

  private String workingDayCode;

  private List<String> deliveryAdrressIDs;

}
