package com.sagag.eshop.repo.entity.order;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "V_CUSTOMER_ORDER_HISTORY")
@Entity
public class VCustomerOrderHistory implements Serializable {

  private static final long serialVersionUID = -895364477362490788L;

  @Id
  private Long id;

  private String orderNumber;

  private String transNumber;

  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;

  private String vehicleInfos;

  private String erpOrderDetailUrl;

  @Column(name = "USER_ID")
  private Long userId;

  private String username;

  @Column(name = "CUSTOMER_NUMBER")
  private Long customerNumber;

  private String orderState;

  private Long finalCustomerOrderId;

  private Integer goodsReceiverId;
}
