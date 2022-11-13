package com.sagag.services.service.response.gtmotive;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@Builder
@EqualsAndHashCode(of = "functionalGroup")
public class GtmotiveFunctionalGroup implements Serializable {

  private static final long serialVersionUID = 1L;

  private String functionalGroup;
  private String functionalGroupDescription;
}
