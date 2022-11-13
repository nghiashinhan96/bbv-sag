package com.sagag.services.gtmotive.domain.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GtmotiveBillingCode extends AbstractGtmotiveAttribute {

  private static final long serialVersionUID = -5426465983689398467L;

  private String code;
  private String description;
}
