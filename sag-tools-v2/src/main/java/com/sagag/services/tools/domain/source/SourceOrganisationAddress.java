package com.sagag.services.tools.domain.source;

import lombok.Data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "ORGANISATION_ADDRESS", schema = "SHOP")
public class SourceOrganisationAddress implements Serializable {

  private static final long serialVersionUID = 5713055081230781346L;

  @Id
  @Column(name = "ORGANISATION_ID")
  private Long organisationId;

  @Column(name = "ADDRESS_ID")
  private Long addressId;
}
