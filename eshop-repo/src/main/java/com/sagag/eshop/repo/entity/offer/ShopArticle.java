package com.sagag.eshop.repo.entity.offer;

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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SHOP_ARTICLE")
public class ShopArticle implements Serializable {

  private static final long serialVersionUID = -7734798987229434675L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "ORGANISATION_ID")
  private Integer organisationId;

  @Column(nullable = false)
  private String type;

  @Column(nullable = false)
  private String articleNumber;

  @Column(nullable = false)
  private String name;

  private String description;

  private Double amount;

  private Double price;

  private Long createdUserId;

  private Date createdDate;

  private Long modifiedUserId;

  private Date modifiedDate;

  private String tecstate;

  @ManyToOne
  private Currency currency;

  private Integer version;
}
