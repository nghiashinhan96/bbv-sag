package com.sagag.services.tools.domain.source;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "SHOP.ORGANISATIONPROPERTY")
public class SourceOrganisationProperty implements Serializable {

  private static final long serialVersionUID = -266557432213541838L;

  @EmbeddedId
  private SourceOrganisationPropertyId sourceOrganisationPropertyId;

  @Column(name = "VALUE")
  private String value;

}
