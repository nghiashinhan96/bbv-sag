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
 * The persistent class for the MIGRATION_LICENSE database table.
 * 
 */
@Entity
@Table(name = "MIGRATION_LICENSE")
@NamedQuery(name = "DestMigrationLicense.findAll", query = "SELECT m FROM DestMigrationLicense m")
@Data
public class DestMigrationLicense implements Serializable {

  private static final long serialVersionUID = -7638677254824525182L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "BEGIN_DATE")
  private Date beginDate;

  @Column(name = "CUSTOMER_NR")
  private Long customerNr;

  @Column(name = "END_DATE")
  private Date endDate;

  @Column(name = "IS_MIGRATED")
  private Boolean isMigrated;

  @Column(name = "LAST_UPDATE")
  private Date lastUpdate;

  @Column(name = "LAST_UPDATED_BY")
  private String lastUpdatedBy;

  @Column(name = "LAST_USED")
  private Date lastUsed;

  @Column(name = "NEW_USER_ID")
  private Long newUserId;

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
