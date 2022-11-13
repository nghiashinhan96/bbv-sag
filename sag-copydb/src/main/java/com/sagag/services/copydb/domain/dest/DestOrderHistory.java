package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the ORDER_HISTORY database table.
 * 
 */
@Entity
@Table(name = "ORDER_HISTORY")
@NamedQuery(name = "DestOrderHistory.findAll", query = "SELECT o FROM DestOrderHistory o")
@Data
public class DestOrderHistory implements Serializable {

  private static final long serialVersionUID = -641147826227415869L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "AX_PROCESS_STATUS")
  private String axProcessStatus;

  @Column(name = "CLOSED_DATE")
  private Date closedDate;

  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "CUSTOMER_NUMBER")
  private Long customerNumber;

  @Column(name = "ERP_ORDER_DETAIL_URL")
  private String erpOrderDetailUrl;

  @Column(name = "FINAL_CUSTOMER_ORDER_ID")
  private Long finalCustomerOrderId;

  @Column(name = "GOODS_RECEIVER_ID")
  private Integer goodsReceiverId;

  @Column(name = "ORDER_DATE")
  private Date orderDate;

  @Column(name = "ORDER_INFO_JSON")
  private String orderInfoJson;

  @Column(name = "ORDER_NUMBER")
  private String orderNumber;

  @Column(name = "ORDER_STATE")
  private String orderState;

  @Column(name = "SALE_ID")
  private Long saleId;

  @Column(name = "SALE_NAME")
  private String saleName;

  @Column(name = "TOTAL")
  private Double total;

  @Column(name = "TRANS_NUMBER")
  private String transNumber;

  @Column(name = "[TYPE]")
  private String type;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "WORK_IDS")
  private String workIds;

}
