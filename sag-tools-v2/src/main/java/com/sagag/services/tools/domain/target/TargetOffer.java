package com.sagag.services.tools.domain.target;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * POJO class of Offer.
 */
@Entity
@Table(name = "OFFER")
@Data
public class TargetOffer implements Serializable {

  private static final long serialVersionUID = -7618061162789460116L;

  @Id
  @GeneratedValue(generator = "specificIdGenerator")
  @GenericGenerator(name = "specificIdGenerator", strategy = "com.sagag.services.tools.support.SpecificIdentityGenerator")
  @Column(name = "ID")
  private Long id;

  @Column(name = "OFFER_NUMBER")
  private String offerNumber;

  @Column(name = "CLIENT_ID")
  private Integer organisationId;

  @Column(name = "OWNER_ID")
  private Long ownerUserId;

  @Column(name = "TYPE")
  private String type;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "OFFER_DATE")
  private Date offerDate;

  @Column(name = "RECIPIENT_ID")
  private Long recipientId;

  @Column(name = "RECIPIENT_ADDRESS_ID")
  private Long recipientAddressId;

  @Column(name = "TOTAL_GROSS_PRICE")
  private Double totalGrossPrice;

  @Column(name = "DELIVERY_DATE")
  private Date deliveryDate;

  @Column(name = "REMARK")
  private String remark;

  @Column(name = "DELIVERY_LOCATION")
  private String deliveryLocation;

  @Column(name = "CREATED_USER_ID")
  private Long createdUserId;

  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "MODIFIED_USER_ID")
  private Long modifiedUserId;

  @Column(name = "MODIFIED_DATE")
  private Date modifiedDate;

  @Column(name = "TECSTATE")
  private String tecstate;

  @Column(name = "CURRENCY_ID")
  private Integer currencyId;

  @Column(name = "VAT")
  private Double vat;

  @Column(name = "ALT_OFFER_PRICE_USED")
  private Boolean altOfferPriceUsed;

  @Column(name = "VERSION")
  private Integer version;
}
