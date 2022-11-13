package com.sagag.services.tools.domain.source;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "SHOP.PERSONPROPERTY")
public class SourcePersonProperty implements Serializable {

  private static final long serialVersionUID = -266557432213541838L;

  @EmbeddedId
  private SourcePersonPropertyId sourcePersonPropertyId;

  @Column(name = "VALUE")
  private String value;

}
