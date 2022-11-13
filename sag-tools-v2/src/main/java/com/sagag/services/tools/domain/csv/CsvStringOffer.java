package com.sagag.services.tools.domain.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class CsvStringOffer implements Serializable {

  private static final long serialVersionUID = -8568788620363961443L;

  @CsvBindByName(column = "ID")
  private String id;

  @CsvBindByName(column = "OFFERNUMBER")
  private String offerNumber;

  @CsvBindByName(column = "VENDOR_ID")
  private String vendorId;

  @CsvBindByName(column = "CLIENT_ID")
  private String clientId; // Should be ignored

  @CsvBindByName(column = "OWNER_ID")
  private String ownerId; // PersonId create offer of end customer

  @CsvBindByName(column = "STATUS")
  private String status;

  @CsvBindByName(column = "OFFERDATE")
  private String offerDate;

  @CsvBindByName(column = "RECIPIENT_ID")
  private String recipientId; // Offer PersonId

  @CsvBindByName(column = "RECIPIENTADDRESS_ID")
  private String recipientAddressId; // Offer person address

  @CsvBindByName(column = "TOTALLONGPRICE")
  private String totalLongPrice;

  @CsvBindByName(column = "DELIVERYDATE")
  private String deliveryDate;

  @CsvBindByName(column = "REMARK")
  private String remark;

  @CsvBindByName(column = "DELIVERYLOCATION")
  private String deliveryLocation;

  @CsvBindByName(column = "CURRENCYISO")
  private String currencyIso;

  @CsvBindByName(column = "VAT")
  private String vat;

  @CsvBindByName(column = "OFFER_TYPE")
  private String offerType;

  @CsvBindByName(column = "ALTOFFERPRICEUSED")
  private String altToOfferPriceUsed;

  @Column(name = "UC_ID")
  @CsvBindByName(column = "UC_ID")
  private String userCreatedId;

  @CsvBindByName(column = "DC")
  private String dateCreated;

  @CsvBindByName(column = "UM_ID")
  private String userModifiedId;

  @CsvBindByName(column = "DM")
  private String dateModified;

  @CsvBindByName(column = "VERSION")
  private String version;

  @CsvBindByName(column = "TECSTATE")
  private String tecstate;
}
