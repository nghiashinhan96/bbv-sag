package com.sagag.services.gtmotive.dto.request.gtinterface;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class GtmotivePartUpdateRequest implements Serializable {

  private static final long serialVersionUID = 1L;
  private String estimateId;
  private String shortNumber;
}
