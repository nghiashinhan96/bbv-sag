package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The primary key class for the OFFER_PERSON_PROPERTY database table.
 * 
 */
@Embeddable
@Data
@EqualsAndHashCode(of = { "personId", "type" })
public class DestOfferPersonPropertyPK implements Serializable {

  private static final long serialVersionUID = -9209212688444848803L;

  @Column(name = "PERSON_ID")
  private long personId;

  @Column(name = "[TYPE]")
  private String type;

}
