package com.sagag.services.copydb.domain.src;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the ARTICLE_HISTORY database table.
 * 
 */
@Entity
@Table(name = "ARTICLE_HISTORY")
@NamedQuery(name = "ArticleHistory.findAll", query = "SELECT a FROM ArticleHistory a")
@Data
public class ArticleHistory implements Serializable {

  private static final long serialVersionUID = 6304335736492153571L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "ART_ID")
  private String artId;

  @Column(name = "ART_IMG")
  private String artImg;

  @Column(name = "ART_NUMBER")
  private String artNumber;

  @Column(name = "ART_SAG_SYS_ID")
  private String artSagSysId;

  @Column(name = "DELIVERY_INFO")
  private String deliveryInfo;

  @Column(name = "GROSS_PRICE")
  private BigDecimal grossPrice;

  @Column(name = "MANUFACTURE")
  private String manufacture;

  @Column(name = "MENGE")
  private Integer menge;

  @Column(name = "REFERENCE")
  private String reference;

  @Column(name = "VEHICLE_INFO")
  private String vehicleInfo;

}
