package com.sagag.services.service.dto.feedback;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class FeedBackSalesOnBehalfUserDataDto extends FeedBackCustomerUserDataDto {

  private static final long serialVersionUID = 4182924224281843115L;
  private Long salesId;
  private String salesEmail;
}
