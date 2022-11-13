package com.sagag.eshop.repo.entity.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * Entity mapping to FINAL_CUSTOMER_ORDER table.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FINAL_CUSTOMER_ORDER")
@Entity
public class FinalCustomerOrder implements Serializable {

  private static final long serialVersionUID = -2945745468226682851L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long userId;

  private Long orgId;

  @Temporal(TemporalType.TIMESTAMP)
  private Date date;

  private String status;

  private Double totalGrossPrice;

  private String reference;

  private String branchRemark;

  private Double totalFinalCustomerNetPrice;

  private Double totalGrossPriceWithVat;

  private Double totalFinalCustomerNetPriceWithVat;

  @Transient
  private List<FinalCustomerOrderItem> items;

}
