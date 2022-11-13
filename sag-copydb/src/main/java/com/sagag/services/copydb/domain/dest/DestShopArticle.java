package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the SHOP_ARTICLE database table.
 * 
 */
@Entity
@Table(name = "SHOP_ARTICLE")
@NamedQuery(name = "DestShopArticle.findAll", query = "SELECT s FROM DestShopArticle s")
@Data
public class DestShopArticle implements Serializable {

  private static final long serialVersionUID = -1648332022771873281L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "AMOUNT")
  private BigDecimal amount;

  @Column(name = "ARTICLE_NUMBER")
  private String articleNumber;

  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "CREATED_USER_ID")
  private Long createdUserId;

  @Column(name = "CURRENCY_ID")
  private Integer currencyId;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "MODIFIED_DATE")
  private Date modifiedDate;

  @Column(name = "MODIFIED_USER_ID")
  private Long modifiedUserId;

  @Column(name = "NAME")
  private String name;

  @Column(name = "ORGANISATION_ID")
  private Integer organisationId;

  @Column(name = "PRICE")
  private BigDecimal price;

  @Column(name = "TECSTATE")
  private String tecstate;

  @Column(name = "[TYPE]")
  private String type;

  @Column(name = "[VERSION]")
  private Integer version;

}
