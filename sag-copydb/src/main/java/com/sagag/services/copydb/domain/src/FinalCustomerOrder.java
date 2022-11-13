package com.sagag.services.copydb.domain.src;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the FINAL_CUSTOMER_ORDER database table.
 * 
 */
@Entity
@Table(name = "FINAL_CUSTOMER_ORDER")
@NamedQuery(name = "FinalCustomerOrder.findAll", query = "SELECT f FROM FinalCustomerOrder f")
@Data
public class FinalCustomerOrder implements Serializable {

  private static final long serialVersionUID = -2945745468226682851L;

  @Id
  @Column(name = "ID")
  private Long id;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "ORG_ID")
  private Long orgId;

  @Column(name = "DATE")
  private Date date;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "TOTAL_GROSS_PRICE")
  private Double totalGrossPrice;

  @Column(name = "REFERENCE")
  private String reference;

  @Column(name = "BRANCH_REMARK")
  private String branchRemark;

}
