package com.sagag.services.copydb.domain.src;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the OFFER_PERSON_PROPERTY database table.
 * 
 */
@Embeddable
@Data
@EqualsAndHashCode(of = {"personId", "type"})
public class OfferPersonPropertyPK implements Serializable {

  private static final long serialVersionUID = -8385592159671676814L;

  @Column(name = "PERSON_ID")
  private long personId;

  private String type;

}
