package com.sagag.services.copydb.domain.src;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the OFFER_POSITION database table.
 * 
 */
@Entity
@Table(name = "OFFER_POSITION")
@NamedQuery(name = "OfferPosition.findAll", query = "SELECT o FROM OfferPosition o")
@Data
public class OfferPosition implements Serializable {

  private static final long serialVersionUID = -1252568897986848873L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "ACTION_TYPE")
  private String actionType;

  @Column(name = "ACTION_VALUE")
  private BigDecimal actionValue;

  @Column(name = "ARTICLE_DESCRIPTION")
  private String articleDescription;

  @Column(name = "ARTICLE_ENHANCED_DESCRIPTION")
  private String articleEnhancedDescription;

  @Column(name = "ARTICLE_NUMBER")
  private String articleNumber;

  @Column(name = "AW_NUMBER")
  private String awNumber;

  @Column(name = "CALCULATED")
  private Date calculated;

  @Column(name = "CATALOG_PATH")
  private String catalogPath;

  @Column(name = "CONNECT_VEHICLE_ID")
  private String connectVehicleId;

  @Column(name = "CONTEXT")
  private String context;

  @Column(name = "CREATED_DATE")
  private String createdDate;

  @Column(name = "CREATED_USER_ID")
  private Long createdUserId;

  @Column(name = "CURRENCY_ID")
  private Integer currencyId;

  @Column(name = "DELIVERY_DATE")
  private String deliveryDate;

  @Column(name = "DELIVERY_TYPE_ID")
  private Integer deliveryTypeId;

  @Column(name = "GROSS_PRICE")
  private BigDecimal grossPrice;

  @Column(name = "MAKE_ID")
  private String makeId;

  @Column(name = "MODEL_ID")
  private String modelId;

  @Column(name = "MODIFIED_DATE")
  private String modifiedDate;

  @Column(name = "MODIFIED_USER_ID")
  private Long modifiedUserId;

  @Column(name = "NET_PRICE")
  private BigDecimal netPrice;

  @Column(name = "OFFER_ID")
  private Long offerId;

  @Column(name = "PRICED_QUANTITY")
  private BigDecimal pricedQuantity;

  @Column(name = "PRICED_UNIT")
  private String pricedUnit;

  @Column(name = "QUANTITY")
  private BigDecimal quantity;

  @Column(name = "REMARK")
  private String remark;

  @Column(name = "SAGSYS_ID")
  private String sagsysId;

  @Column(name = "[SEQUENCE]")
  private Integer sequence;

  @Column(name = "SHOP_ARTICLE_ID")
  private Long shopArticleId;

  @Column(name = "TECSTATE")
  private String tecstate;

  @Column(name = "TOTAL_GROSS_PRICE")
  private BigDecimal totalGrossPrice;

  @Column(name = "[TYPE]")
  private String type;

  @Column(name = "UMSART_ID")
  private String umsartId;

  @Column(name = "UOM_ISO")
  private String uomIso;

  @Column(name = "VEHICLE_BOM_DESCRIPTION")
  private String vehicleBomDescription;

  @Column(name = "VEHICLE_BOM_ID")
  private String vehicleBomId;

  @Column(name = "VEHICLE_DESCRIPTION")
  private String vehicleDescription;

  @Column(name = "VEHICLE_ID")
  private String vehicleId;

  @Column(name = "VEHICLE_TYPE_CODE")
  private String vehicleTypeCode;

  @Column(name = "[VERSION]")
  private Integer version;

}
