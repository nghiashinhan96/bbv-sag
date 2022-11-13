package com.sagag.services.gtmotive.domain.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GtmotiveModel extends AbstractGtmotiveAttribute {

  private static final long serialVersionUID = 3206179817646768652L;

  private String value;
}
