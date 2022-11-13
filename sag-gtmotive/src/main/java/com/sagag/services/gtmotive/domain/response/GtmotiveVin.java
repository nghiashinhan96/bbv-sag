package com.sagag.services.gtmotive.domain.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GtmotiveVin extends AbstractGtmotiveAttribute {

  private static final long serialVersionUID = -223975043687271150L;

  private String value;
}
