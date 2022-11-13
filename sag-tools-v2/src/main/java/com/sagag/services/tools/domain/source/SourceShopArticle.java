package com.sagag.services.tools.domain.source;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "SHOP.SHOPARTICLE")
public class SourceShopArticle implements Serializable {

  private static final long serialVersionUID = 7963385117204236149L;

  @Id
  @Column(name = "ID")
  private Long id;

  @Column(name = "ORGANISATION_ID")
  private Long organisationId;

  @Column(name = "TYPE")
  private String type;

  @Column(name = "ARTICLENUMBER")
  private String articleNumber;

  @Column(name = "NAME")
  private String name;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "AMOUNT", columnDefinition = "number(22,15)")
  private Double amount;

  @Column(name = "PRICE", columnDefinition = "number(22,15)")
  private Double price;

  @Column(name = "UC_ID")
  private Long userCreateId;

  @Column(name = "DC")
  private Date dateCreate;

  @Column(name = "UM_ID")
  private Long userModifyId;

  @Column(name = "DM")
  private Date dateModify;

  @Column(name = "VERSION")
  private Long version;

  @Column(name = "TECSTATE")
  private String tecState;

  @Column(name = "CURRENCYISO")
  private String currencyISO;
}
