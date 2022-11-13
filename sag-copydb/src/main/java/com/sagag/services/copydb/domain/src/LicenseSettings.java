package com.sagag.services.copydb.domain.src;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the LICENSE_SETTINGS database table.
 * 
 */
@Entity
@Table(name = "LICENSE_SETTINGS")
@NamedQuery(name = "LicenseSettings.findAll", query = "SELECT l FROM LicenseSettings l")
@Data
public class LicenseSettings implements Serializable {

  private static final long serialVersionUID = -3350728281465097195L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "LAST_UPDATE")
  private Date lastUpdate;

  @Column(name = "LAST_UPDATED_BY")
  private Long lastUpdatedBy;

  @Column(name = "PACK_ARTICLE_ID")
  private Long packArticleId;

  @Column(name = "PACK_ARTICLE_NO")
  private String packArticleNo;

  @Column(name = "PACK_ID")
  private Long packId;

  @Column(name = "PACK_NAME")
  private String packName;

  @Column(name = "PACK_UMAR_ID")
  private Long packUmarId;

  @Column(name = "PRODUCT_PARAMETERS")
  private String productParameters;

  @Column(name = "QUANTITY")
  private Integer quantity;

  @Column(name = "TYPE_OF_LICENSE")
  private String typeOfLicense;

}
