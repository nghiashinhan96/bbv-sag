package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the EXTERNAL_ORGANISATION database table.
 * 
 */
@Entity
@Table(name = "EXTERNAL_ORGANISATION")
@NamedQuery(name = "ExternalOrganisation.findAll", query = "SELECT e FROM ExternalOrganisation e")
@Data
public class ExternalOrganisation implements Serializable {

  private static final long serialVersionUID = -2641841973772959746L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "EXTERNAL_APP")
  private String externalApp;

  @Column(name = "EXTERNAL_CUSTOMER_ID")
  private String externalCustomerId;

  @Column(name = "EXTERNAL_CUSTOMER_NAME")
  private String externalCustomerName;

  @Column(name = "ORG_ID")
  private Integer orgId;

}
