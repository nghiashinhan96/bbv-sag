package com.sagag.eshop.repo.entity.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity mapping to V_FINAL_CUSTOMER_ORDER_STATUS view.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "V_FINAL_CUSTOMER_ORDER_STATUS")
@Entity
public class VFinalCustomerOrderStatus implements Serializable {

  private static final long serialVersionUID = -349447840171181823L;

  @Id
  private Long orgId;

  private Long orgCode;

  private Integer openOrder;

  private Integer newOrder;

  private Integer placedOrder;

}
