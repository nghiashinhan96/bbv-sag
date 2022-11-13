package com.sagag.services.copydb.domain.src;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the ESHOP_CART_ITEM database table.
 * 
 */
@Entity
@Table(name = "ESHOP_CART_ITEM")
@NamedQuery(name = "EshopCartItem.findAll", query = "SELECT e FROM EshopCartItem e")
@Data
public class EshopCartItem implements Serializable {

  private static final long serialVersionUID = -8960508771145565701L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "ADDED_TIME")
  private Date addedTime;

  @Column(name = "ARTICLE_JSON")
  private String articleJson;

  @Column(name = "CART_KEY")
  private String cartKey;

  @Column(name = "CATEGORY_JSON")
  private String categoryJson;

  @Column(name = "CHILD_JSON")
  private String childJson;

  @Column(name = "CUSTOMER_NR")
  private Long customerNr;

  @Column(name = "ITEM_DESC")
  private String itemDesc;

  @Column(name = "ITEM_TYPE")
  private String itemType;

  @Column(name = "QUANTITY")
  private Integer quantity;

  @Column(name = "USER_ID")
  private String userId;

  @Column(name = "USER_NAME")
  private String userName;

  @Column(name = "VEHICLE_JSON")
  private String vehicleJson;

  @Column(name = "SHOPTYPE")
  private String shopType;

}
