package com.sagag.services.gtmotive.domain.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GtmotiveUmc extends AbstractGtmotiveAttribute {

  private static final long serialVersionUID = 3788194359246517896L;

  private String value;
}
