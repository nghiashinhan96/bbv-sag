package com.sagag.services.hazelcast.domain.haynespro;

import lombok.Data;

import java.io.Serializable;

@Data
public class HaynesProCachePartDto implements Serializable {

  private static final long serialVersionUID = -5175148964213389504L;

  private String genartNumber;
  private String mandatoryPart;
  private String name;
  private String quantity;
}
