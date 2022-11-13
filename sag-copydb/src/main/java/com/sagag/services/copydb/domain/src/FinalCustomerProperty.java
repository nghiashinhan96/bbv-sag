package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the FINAL_CUSTOMER_PROPERTY database table.
 * 
 */
@Entity
@Table(name = "FINAL_CUSTOMER_PROPERTY")
@NamedQuery(name = "FinalCustomerProperty.findAll", query = "SELECT f FROM FinalCustomerProperty f")
@Data
public class FinalCustomerProperty implements Serializable {

  private static final long serialVersionUID = -5012683152894182866L;

  @Id
  @Column(name = "ID")
  private Long id;

  @Column(name = "ORG_ID")
  private Long orgId;

  @Column(name = "SETTING_KEY", nullable = true)
  private String settingKey;

  @Column(name = "VALUE", nullable = false)
  private String value;

}
