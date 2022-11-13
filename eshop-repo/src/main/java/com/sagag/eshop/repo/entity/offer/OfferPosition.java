package com.sagag.eshop.repo.entity.offer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OFFER_POSITION")
public class OfferPosition implements Serializable {

  private static final long serialVersionUID = 1154602785123136573L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "OFFER_ID")
  private Offer offer;

  @Column(nullable = false)
  private String type;

  @Column(name = "UMSART_ID")
  private String umsartId;

  private Long shopArticleId;

  private String articleNumber;

  private String articleDescription;

  private String vehicleId;

  private String vehicleDescription;

  @Column(nullable = false)
  private Date calculated;

  private Integer sequence;

  private String context;

  private Double quantity;

  @Column(nullable = false)
  private String uomIso;

  private Integer currencyId;

  private Double grossPrice;

  private Double netPrice;

  private Double totalGrossPrice;

  private String remark;

  @Column(nullable = false)
  private String actionType;

  private Double actionValue;

  private Integer deliveryTypeId;

  private Date deliveryDate;

  private Long createdUserId;

  private Date createdDate;

  private Long modifiedUserId;

  private Date modifiedDate;

  @Column(nullable = false)
  private String tecstate;

  private String makeId;

  private String modelId;

  private String vehicleBomId;

  private String vehicleBomDescription;

  private String articleEnhancedDescription;

  private String pricedUnit;

  private String catalogPath;

  private String vehicleTypeCode;

  private Double pricedQuantity;

  private int version;

  private String connectVehicleId;

  private String sagsysId;

  @Column(name = "AW_NUMBER")
  private String awNumber;

  private String displayedPriceType;
  private String displayedPriceBrand;
  private Long displayedPriceBrandId;

  @Column(name = "BASKET_ITEM_SOURCE_ID")
  private String basketItemSourceId;

  @Column(name = "BASKET_ITEM_SOURCE_DESC")
  private String basketItemSourceDesc;
}
