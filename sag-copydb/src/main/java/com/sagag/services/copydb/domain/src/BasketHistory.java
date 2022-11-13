package com.sagag.services.copydb.domain.src;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the BASKET_HISTORY database table.
 * 
 */
@Entity
@Table(name = "BASKET_HISTORY")
@NamedQuery(name = "BasketHistory.findAll", query = "SELECT b FROM BasketHistory b")
@Data
public class BasketHistory implements Serializable {

  private static final long serialVersionUID = 947727897889605194L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "ACTIVE")
  private boolean active;

  @Column(name = "BASKET_JSON")
  private String basketJson;

  @Column(name = "BASKET_NAME")
  private String basketName;

  @Column(name = "CREATED_USER_ID")
  private Long createdUserId;

  @Column(name = "CUSTOMER_REF_TEXT")
  private String customerRefText;

  @Column(name = "GRAND_TOTAL_EXCLUDE_VAT")
  private BigDecimal grandTotalExcludeVat;

  @Column(name = "ORGANISATION_ID")
  private Integer organisationId;

  @Column(name = "SALES_USER_ID")
  private Long salesUserId;

  @Column(name = "UPDATED_DATE")
  private Date updatedDate;

}
