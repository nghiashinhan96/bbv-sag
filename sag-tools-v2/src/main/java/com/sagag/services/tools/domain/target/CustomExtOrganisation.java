package com.sagag.services.tools.domain.target;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EXTERNAL_ORGANISATION")
public class CustomExtOrganisation implements Serializable {

  private static final long serialVersionUID = -7554449561276835710L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "ORG_ID")
  private Integer orgId;

  @Column(name = "EXT_CUSTOMER_ID")
  private String extCustomerId;
  
  @Column(name = "EXT_CUST_NAME")
  private String extCustName;

  @Column(name = "COMPANY_NAME")
  private String companyName;
}
