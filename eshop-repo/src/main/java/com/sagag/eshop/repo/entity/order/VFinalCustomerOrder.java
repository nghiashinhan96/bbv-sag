package com.sagag.eshop.repo.entity.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity mapping to V_FINAL_CUSTOMER_ORDER table.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "V_FINAL_CUSTOMER_ORDER")
@Entity
public class VFinalCustomerOrder implements Serializable {

  private static final long serialVersionUID = -2431892108031208992L;

  @Id
  private Long id;

  @Temporal(TemporalType.TIMESTAMP)
  private Date date;

  private String companyName;

  private String address;

  private String postcode;

  private String customerNumber;

  private String status;

  private Long orgId;

  private Double totalGrossPrice;

  private String vehicleDescs;

  private String articleDesc;

  private String username;

  private String reference;

  private String branchRemark;

  private String positionReferences;

  private Double totalFinalCustomerNetPrice;

  private Double totalGrossPriceWithVat;

  private Double totalFinalCustomerNetPriceWithVat;

}
