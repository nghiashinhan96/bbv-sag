package com.sagag.eshop.repo.hz.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "ESHOP_CART_ITEM")
@NamedQuery(name = "EshopCartItem.findAll", query = "SELECT e FROM EshopCartItem e")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EshopCartItem implements Serializable {

  private static final long serialVersionUID = -4165460644881988065L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "CART_KEY")
  private String cartKey;

  @Column(name = "USER_ID")
  private String userId;

  @Column(name = "USER_NAME")
  private String userName;

  @Column(name = "CUSTOMER_NR")
  private String customerNr;

  @Column(name = "ITEM_DESC")
  private String itemDesc;

  @Column(name = "QUANTITY")
  private int quantity;

  @Column(name = "CATEGORY_JSON")
  @Lob
  private String categoryJson;

  @Column(name = "VEHICLE_JSON")
  @Lob
  private String vehicleJson;

  @Column(name = "ARTICLE_JSON")
  @Lob
  private String articleJson;

  @Column(name = "ITEM_TYPE")
  private String itemType;

  @Column(name = "ADDED_TIME")
  private Date addedTime;

  @Column(name = "ATTACHED_ARTICLES_JSON")
  @Lob
  private String attachedArticlesJson;

  @Column(name = "SHOPTYPE")
  private String shopType;

  @Column(name = "BASKET_ITEM_SOURCE_ID")
  private String basketItemSourceId;

  @Column(name = "BASKET_ITEM_SOURCE_DESC")
  private String basketItemSourceDesc;
}
