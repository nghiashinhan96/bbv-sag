package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MIGRATION_DVSE_PROFILE_SETTINGS database table.
 * 
 */
@Entity
@Table(name = "MIGRATION_DVSE_PROFILE_SETTINGS")
@NamedQuery(name = "DestMigrationDvseProfileSettings.findAll", query = "SELECT m FROM DestMigrationDvseProfileSettings m")
@Data
public class DestMigrationDvseProfileSettings implements Serializable {

  private static final long serialVersionUID = 4227745436783689013L;

  @Column(name = "AFFILIATE")
  private String affiliate;

  @Column(name = "CART_NETTO_OPTION")
  private String cartNettoOption;

  @Column(name = "CART_VAT_OPTION")
  private String cartVatOption;

  @Column(name = "DELIVERY_ADDRESS_ID")
  private Long deliveryAddressId;

  @Column(name = "DELIVERY_METHOD")
  private String deliveryMethod;

  @Column(name = "EMAIL_CONFIRMATION")
  private short emailConfirmation;

  @Column(name = "EMAIL_SHOW_NET_PRICES")
  private short emailShowNetPrices;

  @Column(name = "HOURLY_RATE")
  private Integer hourlyRate;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "INVOICE_ADDRESS_ID")
  private Long invoiceAddressId;

  @Column(name = "INVOICE_TYPE")
  private String invoiceType;

  @Column(name = "OLD_ID")
  private Integer oldId;

  @Column(name = "OLD_ID1")
  private Integer oldId1;

  @Column(name = "PARTIAL_DELIVERY")
  private short partialDelivery;

  @Column(name = "PAYMENT_TYPE")
  private String paymentType;

  @Column(name = "SHOW_NET_PRICES")
  private short showNetPrices;

  @Column(name = "USER_ID")
  private Integer userId;

}
