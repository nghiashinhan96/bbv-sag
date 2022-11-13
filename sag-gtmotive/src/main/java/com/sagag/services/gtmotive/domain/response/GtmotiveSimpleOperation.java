package com.sagag.services.gtmotive.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GtmotiveSimpleOperation implements Serializable {

  private static final long serialVersionUID = -717248160435244706L;

  private String reference;
  private String partDescription;

}
