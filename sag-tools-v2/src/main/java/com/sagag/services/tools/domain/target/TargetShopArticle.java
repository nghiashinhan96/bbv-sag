package com.sagag.services.tools.domain.target;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "SHOP_ARTICLE")
public class TargetShopArticle implements Serializable {

  private static final long serialVersionUID = 2602446515644073675L;

  @Id
  @GeneratedValue(generator = "specificIdGenerator")
  @GenericGenerator(name = "specificIdGenerator", strategy = "com.sagag.services.tools.support.SpecificIdentityGenerator")
  @Column(name = "ID")
  private Long id;

  @Column(name = "ORGANISATION_ID")
  private Integer organisationId;

  @Column(name = "TYPE")
  private String type;

  @Column(name = "ARTICLE_NUMBER")
  private String articleNumber;

  @Column(name = "NAME")
  private String name;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "AMOUNT")
  private Double amount;

  @Column(name = "PRICE")
  private Double price;

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

  @Column(name = "VERSION")
  private Integer version;
}
