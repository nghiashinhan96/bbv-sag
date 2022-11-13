package com.sagag.services.copydb.domain.dest;

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
@NamedQuery(name = "DestArticleHistory.findAll", query = "SELECT a FROM DestArticleHistory a")
@Data
public class DestArticleHistory implements Serializable {

  private static final long serialVersionUID = -5486980759858396917L;

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
