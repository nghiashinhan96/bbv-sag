package com.sagag.services.tools.domain.target;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "LICENSE")
@Entity
@Data
@Builder
public class License implements Serializable {

  private static final long serialVersionUID = -6432600255055612624L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "TYPE_OF_LICENSE")
  private String typeOfLicense;

  @Column(name = "PACK_ID")
  private Long packId;

  @Column(name = "PACK_NAME")
  private String packName;

  @Column(name = "CUSTOMER_NR")
  private Long customerNr;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "BEGIN_DATE")
  private Date beginDate;

  @Column(name = "END_DATE")
  private Date endDate;

  @Column(name = "QUANTITY")
  private Integer quantity;

  @Column(name = "QUANTITY_USED")
  private Integer quantityUsed;

  @Column(name = "LAST_USED")
  private Date lastUsed;

  @Column(name = "LAST_UPDATE")
  private Date lastUpdate;

  @Column(name = "LAST_UPDATED_BY")
  private Long lastUpdateBy;
}
