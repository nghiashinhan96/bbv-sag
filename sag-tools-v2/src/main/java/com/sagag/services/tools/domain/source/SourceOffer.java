package com.sagag.services.tools.domain.source;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "SHOP.OFFER")
public class SourceOffer implements Serializable {

  private static final long serialVersionUID = -8785370610102074241L;

  @Id
  @Column(name = "ID")
  private Long id;

  @Column(name = "OFFERNUMBER")
  private String offerNumber;

  @Column(name = "VENDOR_ID")
  private Long vendorId;

  @Column(name = "CLIENT_ID")
  private Long clientId;

  @Column(name = "OWNER_ID")
  private Long ownerId;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "OFFERDATE")
  private Date offerDate;

  @Column(name = "RECIPIENT_ID")
  private Long recipientId;

  @Column(name = "RECIPIENTADDRESS_ID")
  private Long recipientAddressId;

  @Column(name = "TOTALLONGPRICE", columnDefinition = "number(22,15)")
  private Double totalLongPrice;

  @Column(name = "DELIVERYDATE")
  private Date deliveryDate;

  @Column(name = "REMARK")
  private String remark;

  @Column(name = "DELIVERYLOCATION")
  private String deliveryLocation;

  @Column(name = "UC_ID")
  private Long userCreateId;

  @Column(name = "DC")
  private Date dateCreate;

  @Column(name = "UM_ID")
  private Long userModifyId;

  @Column(name = "DM")
  private Date dateModify;

  @Column(name = "VERSION")
  private Long version;

  @Column(name = "TECSTATE")
  private String tecState;

  @Column(name = "CURRENCYISO")
  private String currencyIso;

  @Column(name = "VAT", columnDefinition = "number(22,15)")
  private Double vat;

  @Column(name = "OFFER_TYPE")
  private String offerType;

  @Column(name = "ALTOFFERPRICEUSED")
  private Boolean altOfferPriceUsed;
  
  @OneToMany(mappedBy = "offer")
  private List<SourceOfferPosition> offerPositions;

}
