package com.sagag.services.service.response.gtmotive;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class GtmotivePartItem implements Serializable {

  private static final long serialVersionUID = 57698155283071308L;
  private String partCode;
  private String partDescription;
}
