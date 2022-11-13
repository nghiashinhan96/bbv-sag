package com.sagag.services.gtmotive.domain.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RegistrationNumber extends AbstractGtmotiveAttribute {

  private static final long serialVersionUID = -8394865717046380612L;

  private String value;
}
