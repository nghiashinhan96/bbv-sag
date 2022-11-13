package com.sagag.eshop.repo.entity.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity mapping to ORDER_HISTORY table.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ORDER_HISTORY")
@Entity
public class OrderHistory implements Serializable {

  private static final long serialVersionUID = 1111304556587369200L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String orderNumber;

  private String transNumber;

  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;

  private String saleName;

  private float total;

  @Lob
  private String orderInfoJson;

  private String erpOrderDetailUrl;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "CUSTOMER_NUMBER")
  private Long customerNumber;

  private Long saleId;

  private Date closedDate;

  private String type;

  private String axProcessStatus;

  private String workIds;

  private String orderState;

  private Long finalCustomerOrderId;

  private Integer goodsReceiverId;

  private String vehicleInfos;

}
