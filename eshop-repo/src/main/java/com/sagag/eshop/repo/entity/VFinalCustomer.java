package com.sagag.eshop.repo.entity;

import lombok.Data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "V_FINAL_CUSTOMER")
public class VFinalCustomer implements Serializable {

  private static final long serialVersionUID = 762700880570207842L;

  @Id
  private int orgId;

  private String name;

  private String description;

  private String finalCustomerType;

  private String addressInfo;

  private String contactInfo;

  private int parentOrgId;

  private String status;

  private Boolean hasInProgressOrders;
}
