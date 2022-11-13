package com.sagag.services.tools.domain.source;

import lombok.Data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "SHOP.ORGANISATIONLINK")
public class SourceOrganisationLink implements Serializable {

  private static final long serialVersionUID = -1831250537429098989L;

  @EmbeddedId
  @Column(name = "ID")
  private Long id;

  @Column(name = "VENDOR_ID")
  private Long vendorId;

  @Column(name = "CLIENT_ID")
  private Long clientId;

  @Column(name = "SALESORG_ID")
  private Long salesOrgId;

  @Column(name = "TYPE")
  private String type;
}
