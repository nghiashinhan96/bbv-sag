package com.sagag.eshop.repo.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity of ARTICLE_CUSTOM_VATRATE_MAP table.
 *
 */
@Data
@Entity
@Table(name = "ARTICLE_CUSTOM_VATRATE_MAP")
public class VatRate implements Serializable {

  private static final long serialVersionUID = 4453768119407413182L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "ART_ID")
  private String artId;

  @Column(name = "CUSTOM_VAT_RATE")
  private Double customVatRate;

  @Column(name = "COMMENT")
  private String comment;
}
