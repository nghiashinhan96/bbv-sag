package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the COLLECTION_RELATION database table.
 * 
 */
@Entity
@Table(name = "COLLECTION_RELATION")
@NamedQuery(name = "DestCollectionRelation.findAll", query = "SELECT c FROM DestCollectionRelation c")
@Data
public class DestCollectionRelation implements Serializable {

  private static final long serialVersionUID = -7877653629610705063L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "COLLECTION_ID")
  private Integer collectionId;

  @Column(name = "ORGANISATION_ID")
  private Integer organisationId;

  @Column(name = "IS_ACTIVE")
  private Boolean isActive;
}
