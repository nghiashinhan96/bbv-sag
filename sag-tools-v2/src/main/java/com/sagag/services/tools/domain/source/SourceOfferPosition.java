package com.sagag.services.tools.domain.source;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "SHOP.OFFERPOSITION")
public class SourceOfferPosition implements Serializable {

  private static final long serialVersionUID = 1076826603162658968L;

  @Id
  @Column(name = "ID")
  private Long id;

  @Column(name = "OFFER_ID")
  private Long offerId;

  @Column(name = "TYPE")
  private String type;

  @Column(name = "UMSART_ID")
  private Long umsartId;

  @Column(name = "SHOPARTICLE_ID")
  private Long shopArticleId;

  @Column(name = "ARTICLENUMBER")
  private String articleNumber;

  @Column(name = "ARTICLEDESCRIPTION")
  private String articleDescription;

  @Column(name = "VEHICLE_ID")
  private String vehicleId;

  @Column(name = "VEHICLEDESCRIPTION")
  private String vehicleDescription;

  @Column(name = "CALCULATED")
  private Date calculated;

  @Column(name = "SEQUENCE")
  private Long sequence;

  @Column(name = "CONTEXT")
  private String context;

  @Column(name = "QUANTITY")
  private Double quantity;

  @Column(name = "UOMISO")
  private String uomIso;

  @Column(name = "CURRENCYISO")
  private String currencyIso;

  @Column(name = "LONGPRICE")
  private Double longPrice;

  @Column(name = "NETPRICE")
  private Double netPrice;

  @Column(name = "TOTALLONGPRICE")
  private Double totalLongPrice;

  @Column(name = "REMARK")
  private String remark;

  @Column(name = "OFFERACTION_TYPE")
  private String offerActionType;

  @Column(name = "DELIVERYTYPE")
  private String deliveryType;

  @Column(name = "DELIVERYDATE")
  private Date deliveryDate;

  @Column(name = "MAKE_ID")
  private Long makeId;

  @Column(name = "MODEL_ID")
  private Long modelId;

  @Column(name = "OFFERACTION_VALUE")
  private Double offerActionValue;

  @Column(name = "VEHICLEBOM_ID")
  private Long vehicleBomId;

  @Column(name = "VEHICLEBOMDESCRIPTION")
  private String vehicleBomDescription;

  @Column(name = "ARTICLEENHANCEDDESCRIPTION")
  private String articleEnhanceDescription;

  @Column(name = "PRICEDUNIT")
  private String pricedUnit;

  @Column(name = "CATALOGPATH")
  private String catalogPath;

  @Column(name = "VEHICLETYPECODE")
  private String vehicleTypeCode;

  @Column(name = "PRICEDQUANTITY")
  private Double pricedQuantity;

  @Column(name = "UC_ID")
  private Long userCreatedId;

  @Column(name = "DC")
  private Date dateCreated;

  @Column(name = "UM_ID")
  private Long userModifiedId;

  @Column(name = "DM")
  private Date dateModified;

  @Column(name = "VERSION")
  private Integer version;

  @Column(name = "TECSTATE")
  private String tecstate;
  
  @ManyToOne
  @JoinColumn(name = "OFFER_ID", nullable = false, insertable = false, updatable = false)
  private SourceOffer offer;
}
