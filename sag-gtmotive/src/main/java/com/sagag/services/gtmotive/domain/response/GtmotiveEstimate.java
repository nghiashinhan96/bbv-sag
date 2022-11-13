package com.sagag.services.gtmotive.domain.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GtmotiveEstimate extends AbstractGtmotiveAttribute {

  private static final long serialVersionUID = -1912038226261561342L;

  private String id;
}
