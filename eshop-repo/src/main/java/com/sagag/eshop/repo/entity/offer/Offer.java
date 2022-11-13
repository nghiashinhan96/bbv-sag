package com.sagag.eshop.repo.entity.offer;

import com.sagag.eshop.repo.entity.Organisation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "OFFER")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Offer implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 6824433999752914297L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String offerNumber;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CLIENT_ID")
  private Organisation organisation;

  @Column(name = "OWNER_ID", nullable = false)
  private Long ownerId;

  @Column(nullable = false)
  private String type;

  private String status;

  private Date offerDate;
  @ManyToOne
  @JoinColumn(name = "RECIPIENT_ID")
  private OfferPerson recipient;

  @ManyToOne
  @JoinColumn(name = "RECIPIENT_ADDRESS_ID")
  private OfferAddress recipientAddress;

  @Column(nullable = false)
  private Double totalGrossPrice;

  private Date deliveryDate;

  private String remark;

  private String deliveryLocation;

  private Long createdUserId;

  private Date createdDate;

  private Long modifiedUserId;

  private Date modifiedDate;

  private String tecstate;

  @ManyToOne
  private Currency currency;

  private Double vat;

  private Boolean altOfferPriceUsed;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "offer", cascade = { CascadeType.ALL })
  private List<OfferPosition> offerPositions;

  private Integer version;
}
