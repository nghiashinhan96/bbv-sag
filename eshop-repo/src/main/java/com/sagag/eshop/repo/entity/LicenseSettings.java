package com.sagag.eshop.repo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "LICENSE_SETTINGS")
@Entity
@NamedQueries(value = {
    @NamedQuery(name = "LicenseSettings.findAll", query = "SELECT l FROM LicenseSettings l"),
    @NamedQuery(name = "LicenseSettings.findById",
        query = "SELECT l from LicenseSettings l where l.id= :id"),
    @NamedQuery(name = "LicenseSettings.findByPackId",
        query = "SELECT l from LicenseSettings l where l.packId= :packId") })
@Data
public class LicenseSettings implements Serializable {

  private static final long serialVersionUID = -6432600255055612624L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "PACK_ID")
  private long packId;

  @Column(name = "PACK_NAME")
  private String packName;

  @Column(name = "PACK_ARTICLE_ID")
  private long packArticleId;

  @Column(name = "PACK_ARTICLE_NO")
  private String packArticleNo;

  @Column(name = "PACK_UMAR_ID")
  private String packUmarId;

  @Column(name = "QUANTITY")
  private int quantity;

  @Column(name = "LAST_UPDATE")
  private Date lastUpdate;

  @Column(name = "LAST_UPDATED_BY")
  private String lastUpdatedBy;

  @Column(name = "TYPE_OF_LICENSE")
  private String typeOfLicense;

  @Column(name = "PRODUCT_TEXT")
  private String productText;

  @Column(name = "OPTIONAL_PARAMS")
  private String optionalParameters;
}
