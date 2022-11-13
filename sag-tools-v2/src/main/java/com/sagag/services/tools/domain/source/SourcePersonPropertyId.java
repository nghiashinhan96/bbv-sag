package com.sagag.services.tools.domain.source;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class SourcePersonPropertyId implements Serializable {

  private static final long serialVersionUID = 7343859730282785770L;

  @Column(name = "PERSON_ID")
  private Long personId;

  @Column(name = "TYPE")
  private String type;
}
