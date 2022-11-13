package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the FINAL_CUSTOMER_ORDER_ITEM database table.
 * 
 */
@Entity
@Table(name = "FINAL_CUSTOMER_ORDER_ITEM")
@NamedQuery(name = "FinalCustomerOrderItem.findAll", query = "SELECT f FROM FinalCustomerOrderItem f")
@Data
public class FinalCustomerOrderItem implements Serializable {

  private static final long serialVersionUID = -283079023483744287L;

  @Id
  @Column(name = "ID")
  private Long id;

  @Column(name = "FINAL_CUSTOMER_ORDER_ID")
  private Long finalCustomerOrderId;

  @Column(name = "VEHICLE_ID")
  private String vehicleId;

  @Column(name = "VEHICLE_DESC")
  private String vehicleDesc;

  @Column(name = "ARTICLE_ID")
  private String articleId;

  @Column(name = "ARTICLE_DESC")
  private String articleDesc;

  @Column(name = "QUANTITY")
  private Integer quantity;

  @Column(name = "TYPE")
  private String type;

  @Column(name = "GEN_ART_DESCRIPTION")
  private String genArtDescription;

  @Column(name = "SUPPLIER")
  private String supplier;

  @Column(name = "BRAND")
  private String brand;

  @Column(name = "IMAGES")
  private String images;

  @Column(name = "REFERENCE")
  private String reference;

  @Column(name = "GROSS_PRICE")
  private Double grossPrice;

  @Column(name = "NET_PRICE")
  private Double netPrice;

  @Column(name = "SALES_QUANTITY")
  private Integer salesQuantity;

  @Column(name = "PRODUCT_ADDON")
  private String productAddon;
}
