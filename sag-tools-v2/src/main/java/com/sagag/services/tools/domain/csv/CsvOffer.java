package com.sagag.services.tools.domain.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.sagag.services.tools.domain.SourceBaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class CsvOffer extends SourceBaseObject {

  private static final long serialVersionUID = -8568788620363961443L;

  @CsvBindByName(column = "ID", locale = "US")
  private Long id;

  @CsvBindByName(column = "OFFERNUMBER", locale = "US")
  private String offerNumber;

  @CsvBindByName(column = "VENDOR_ID", locale = "US")
  private Long vendorId;

  @CsvBindByName(column = "CLIENT_ID", locale = "US")
  private Long clientId; // Should be ignored

  @CsvBindByName(column = "OWNER_ID", locale = "US")
  private Long ownerId; // PersonId create offer of end customer

  @CsvBindByName(column = "STATUS", locale = "US")
  private String status;

  @CsvBindByName(column = "OFFERDATE", locale = "US")
  @CsvDate("yyyy-MM-dd HH:mm:ss")
  private Date offerDate;

  @CsvBindByName(column = "RECIPIENT_ID", locale = "US")
  private Long recipientId; // Offer PersonId

  @CsvBindByName(column = "RECIPIENTADDRESS_ID", locale = "US")
  private Long recipientAddressId; // Offer person address

  @CsvBindByName(column = "TOTALLONGPRICE", locale = "US")
  private Double totalLongPrice;

  @CsvBindByName(column = "DELIVERYDATE", locale = "US")
  @CsvDate("yyyy-MM-dd HH:mm:ss")
  private Date deliveryDate;

  @CsvBindByName(column = "REMARK", locale = "US")
  private String remark;

  @CsvBindByName(column = "DELIVERYLOCATION", locale = "US")
  private String deliveryLocation;

  @CsvBindByName(column = "CURRENCYISO", locale = "US")
  private String currencyIso;

  @CsvBindByName(column = "VAT", locale = "US")
  private Double vat;

  @CsvBindByName(column = "OFFER_TYPE", locale = "US")
  private String offerType;

  @CsvBindByName(column = "ALTOFFERPRICEUSED", locale = "US")
  private Boolean altToOfferPriceUsed;
}
