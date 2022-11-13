package com.sagag.eshop.repo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQuery(name = "WssMarginsBrand.findAll", query = "SELECT o FROM WssMarginsBrand o")
@Data
@Builder
@Table(name = "WSS_MARGINS_BRAND")
@NoArgsConstructor
@AllArgsConstructor
public class WssMarginsBrand implements Serializable {

  private static final long serialVersionUID = -7734772310329240318L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "ORG_ID", nullable = false)
  private Integer orgId;

  @Column(name = "BRAND_ID")
  private Integer brandId;

  @Column(name = "BRAND_NAME")
  private String brandName;

  @Column(name = "MARGIN_1")
  private Double margin1;

  @Column(name = "MARGIN_2")
  private Double margin2;

  @Column(name = "MARGIN_3")
  private Double margin3;

  @Column(name = "MARGIN_4")
  private Double margin4;

  @Column(name = "MARGIN_5")
  private Double margin5;

  @Column(name = "MARGIN_6")
  private Double margin6;

  @Column(name = "MARGIN_7")
  private Double margin7;

  @Column(name = "IS_DEFAULT", nullable = false)
  private boolean isDefault;

}
