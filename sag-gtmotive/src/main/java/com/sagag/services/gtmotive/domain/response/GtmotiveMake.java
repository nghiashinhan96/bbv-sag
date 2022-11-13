package com.sagag.services.gtmotive.domain.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GtmotiveMake extends AbstractGtmotiveAttribute {

  private static final long serialVersionUID = -5665630395452192467L;

  private String value;
}
