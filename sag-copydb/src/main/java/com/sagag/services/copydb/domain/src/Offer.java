package com.sagag.services.copydb.domain.src;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the OFFER database table.
 * 
 */
@Entity
@Table(name = "OFFER")
@NamedQuery(name = "Offer.findAll", query = "SELECT o FROM Offer o")
@Data
public class Offer implements Serializable {

  private static final long serialVersionUID = -983386909263900411L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "ALT_OFFER_PRICE_USED")
  private Boolean altOfferPriceUsed;

  @Column(name = "CLIENT_ID")
  private Integer clientId;

  @Column(name = "CREATED_DATE")
  private String createdDate;

  @Column(name = "CREATED_USER_ID")
  private Long createdUserId;

  @Column(name = "CURRENCY_ID")
  private Integer currencyId;

  @Column(name = "DELIVERY_DATE")
  private String deliveryDate;

  @Column(name = "DELIVERY_LOCATION")
  private String deliveryLocation;

  @Column(name = "MODIFIED_DATE")
  private String modifiedDate;

  @Column(name = "MODIFIED_USER_ID")
  private Long modifiedUserId;

  @Column(name = "OFFER_DATE")
  private String offerDate;

  @Column(name = "OFFER_NUMBER")
  private String offerNumber;

  @Column(name = "OWNER_ID")
  private Long ownerId;

  @Column(name = "RECIPIENT_ADDRESS_ID")
  private Long recipientAddressId;

  @Column(name = "RECIPIENT_ID")
  private Long recipientId;

  @Column(name = "REMARK")
  private String remark;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "TECSTATE")
  private String tecstate;

  @Column(name = "TOTAL_GROSS_PRICE")
  private BigDecimal totalGrossPrice;

  @Column(name = "[TYPE]")
  private String type;

  @Column(name = "VAT")
  private BigDecimal vat;

  @Column(name = "[VERSION]")
  private Integer version;

}
