package com.sagag.services.tools.domain.target;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "COLLECTION_RELATION")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectionRelation implements Serializable {

  private static final long serialVersionUID = -7877653629610705063L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "COLLECTION_ID")
  private Integer collectionId;

  @Column(name = "ORGANISATION_ID")
  private Integer organisationId;

  @Column(name = "IS_ACTIVE")
  private Boolean isActive;
}
