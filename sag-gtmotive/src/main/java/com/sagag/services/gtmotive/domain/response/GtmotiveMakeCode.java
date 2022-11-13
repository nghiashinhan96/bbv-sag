package com.sagag.services.gtmotive.domain.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GtmotiveMakeCode extends AbstractGtmotiveAttribute {

  private static final long serialVersionUID = 7777575597463802125L;

  private String value;
}
