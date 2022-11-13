package com.sagag.eshop.repo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Basket history view class which maps to view V_BASKET_HISTORY.
 */
@Data
@Entity
@Table(name = "V_BASKET_HISTORY")
public class VBasketHistory implements Serializable {

  private static final long serialVersionUID = -3010000719270971556L;

  @Id
  private Long id;

  private Long basketHistoryId;

  private String basketName;

  private Double grandTotalExcludeVat;

  @Lob
  private String basketJson;

  private Date updatedDate;

  private boolean active;

  private Integer orgId;

  private String orgCode;

  private String customerName;

  private Long createdUserId;

  private String createdFirstName;

  private String createdLastName;

  private String createdFullName;

  private Long salesUserId;

  private String salesFirstName;

  private String salesLastName;

  private String salesFullName;

  private String customerRefText;
}
