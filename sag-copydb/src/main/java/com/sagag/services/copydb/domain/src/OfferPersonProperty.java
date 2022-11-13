package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

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
@NamedQuery(name = "OfferPersonProperty.findAll", query = "SELECT o FROM OfferPersonProperty o")
@Data
public class OfferPersonProperty implements Serializable {

  private static final long serialVersionUID = 2923063552380131237L;

  @EmbeddedId
  private OfferPersonPropertyPK id;

  private String value;

}
