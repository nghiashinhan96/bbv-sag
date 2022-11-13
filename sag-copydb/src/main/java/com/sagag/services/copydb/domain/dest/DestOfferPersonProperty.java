package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the OFFER_PERSON_PROPERTY database table.
 * 
 */
@Entity
@Table(name = "OFFER_PERSON_PROPERTY")
@NamedQuery(name = "DestOfferPersonProperty.findAll", query = "SELECT o FROM DestOfferPersonProperty o")
@Data
public class DestOfferPersonProperty implements Serializable {

  private static final long serialVersionUID = -2580155454497687716L;

  @EmbeddedId
  private DestOfferPersonPropertyPK id;

  @Column(name = "[VALUE]")
  private String value;

}
