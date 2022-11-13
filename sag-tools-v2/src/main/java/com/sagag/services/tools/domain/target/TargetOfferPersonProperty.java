package com.sagag.services.tools.domain.target;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "OFFER_PERSON_PROPERTY")
public class TargetOfferPersonProperty implements Serializable {

  private static final long serialVersionUID = -1006900113344912948L;

  @EmbeddedId
  private TargetOfferPersonPropertyId targetOfferPersonPropertyId;

  @Column(name = "VALUE")
  private String value;

}
