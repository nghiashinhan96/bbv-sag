package com.sagag.services.tools.domain.target;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class TargetOfferPersonPropertyId implements Serializable {

  private static final long serialVersionUID = 1557199486008465930L;

  @Column(name = "PERSON_ID")
  private Long personId;

  @Column(name = "TYPE")
  private String type;
}
