package com.sagag.services.copydb.domain.src;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "LICENSE")
@NamedQuery(name = "License.findAll", query = "SELECT l FROM License l")
@Data
public class License implements Serializable {

  private static final long serialVersionUID = -6432600255055612624L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "BEGIN_DATE")
  private Date beginDate;

  @Column(name = "CUSTOMER_NR")
  private Long customerNr;

  @Column(name = "END_DATE")
  private Date endDate;

  @Column(name = "LAST_UPDATE")
  private Date lastUpdate;

  @Column(name = "LAST_UPDATED_BY")
  private Long lastUpdatedBy;

  @Column(name = "LAST_USED")
  private Date lastUsed;

  @Column(name = "PACK_ID")
  private Long packId;

  @Column(name = "PACK_NAME")
  private String packName;

  @Column(name = "QUANTITY")
  private Integer quantity;

  @Column(name = "QUANTITY_USED")
  private Integer quantityUsed;

  @Column(name = "TYPE_OF_LICENSE")
  private String typeOfLicense;

  @Column(name = "USER_ID")
  private Long userId;
}
