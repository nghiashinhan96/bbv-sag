package com.sagag.eshop.repo.entity;

import com.sagag.eshop.repo.enums.ArticleShopType;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table(name = "SUPPORTED_BRAND_PROMOTION")
public class SupportedBrandPromotion implements Serializable {

  private static final long serialVersionUID = -5817196059490397883L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Enumerated(EnumType.STRING)
  private ArticleShopType articleShopType;

  private Integer supportedAffiliateId;

  private String brand;

  private boolean active;

  private Date startDate;

  private Date endDate;

  private Date createdDate;

  private Date createdUserId;

  private Date modifiedDate;

  private Long modifiedUserId;
}
