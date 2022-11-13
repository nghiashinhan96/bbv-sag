package com.sagag.services.tools.domain.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class CsvStringOfferPosition implements Serializable {

  private static final long serialVersionUID = 5925280819493205583L;

  @CsvBindByName(column = "ID", locale = "US")
  private String id;

  @CsvBindByName(column = "OFFER_ID", locale = "US")
  private String offerId;

  @CsvBindByName(column = "TYPE", locale = "US")
  private String type;

  @CsvBindByName(column = "UMSART_ID", locale = "US")
  private String umsartId;

  @CsvBindByName(column = "SHOPARTICLE_ID", locale = "US")
  private String shopArticleId;

  @CsvBindByName(column = "ARTICLENUMBER", locale = "US")
  private String articleNumber;

  @CsvBindByName(column = "ARTICLEDESCRIPTION", locale = "US")
  private String articleDescription;

  @CsvBindByName(column = "VEHICLE_ID", locale = "US")
  private String vehicleId;

  @CsvBindByName(column = "VEHICLEDESCRIPTION", locale = "US")
  private String vehicleDescription;

  @CsvBindByName(column = "CALCULATED", locale = "US")
  private String calculated;

  @CsvBindByName(column = "SEQUENCE", locale = "US")
  private String sequence;

  @CsvBindByName(column = "CONTEXT", locale = "US")
  private String context;

  @CsvBindByName(column = "QUANTITY", locale = "US")
  private String quantity;

  @CsvBindByName(column = "UOMISO", locale = "US")
  private String uomIso;

  @CsvBindByName(column = "CURRENCYISO", locale = "US")
  private String currencyIso;

  @CsvBindByName(column = "LONGPRICE", locale = "US")
  private String longPrice;

  @CsvBindByName(column = "NETPRICE", locale = "US")
  private String netPrice;

  @CsvBindByName(column = "TOTALLONGPRICE", locale = "US")
  private String totalLongPrice;

  @CsvBindByName(column = "REMARK", locale = "US")
  private String remark;

  @CsvBindByName(column = "OFFERACTION_TYPE", locale = "US")
  private String offerActionType;

  @CsvBindByName(column = "DELIVERYTYPE", locale = "US")
  private String deliveryType;

  @CsvBindByName(column = "DELIVERYDATE", locale = "US")
  private String deliveryDate;

  @CsvBindByName(column = "MAKE_ID", locale = "US")
  private String makeId;

  @CsvBindByName(column = "MODEL_ID", locale = "US")
  private String modelId;

  @CsvBindByName(column = "OFFERACTION_VALUE", locale = "US")
  private String offerActionValue;

  @CsvBindByName(column = "VEHICLEBOM_ID", locale = "US")
  private String vehicleBomId;

  @CsvBindByName(column = "VEHICLEBOMDESCRIPTION", locale = "US")
  private String vehicleBomDescription;

  @CsvBindByName(column = "ARTICLEENHANCEDDESCRIPTION", locale = "US")
  private String articleEnhanceDescription;

  @CsvBindByName(column = "PRICEDUNIT", locale = "US")
  private String pricedUnit;

  @CsvBindByName(column = "CATALOGPATH", locale = "US")
  private String catalogPath;

  @CsvBindByName(column = "VEHICLETYPECODE", locale = "US")
  private String vehicleTypeCode;

  @CsvBindByName(column = "PRICEDQUANTITY", locale = "US")
  private String pricedQuantity;

  @Column(name = "UC_ID")
  @CsvBindByName(column = "UC_ID", locale = "US")
  private String userCreatedId;

  @CsvBindByName(column = "DC", locale = "US")
  private String dateCreated;

  @CsvBindByName(column = "UM_ID", locale = "US")
  private String userModifiedId;

  @CsvBindByName(column = "DM", locale = "US")
  private String dateModified;

  @CsvBindByName(column = "VERSION", locale = "US")
  private String version;

  @CsvBindByName(column = "TECSTATE")
  private String tecstate;
}
