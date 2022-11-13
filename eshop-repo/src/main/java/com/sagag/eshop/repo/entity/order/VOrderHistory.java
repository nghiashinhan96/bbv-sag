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
 * Entity mapping to V_ORDER_HISTORY view.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "V_ORDER_HISTORY")
@Entity
public class VOrderHistory implements Serializable {

  private static final long serialVersionUID = 1111304556587369200L;

  @Id
  private Long id;

  private String customerNumber;

  private String customerName;

  private String orderNumber;

  private float totalPrice;

  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;

  private String saleName;

  private Long saleId;

  private Long affiliateId;

  @Temporal(TemporalType.TIMESTAMP)
  private Date closedDate;

  private String type;

  private String axProcessStatus;

  private String workIds;

  private String axOrderUrl;

}
