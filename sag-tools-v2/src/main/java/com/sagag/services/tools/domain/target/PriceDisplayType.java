package com.sagag.services.tools.domain.target;

import lombok.Data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "PRICE_DISPLAY_TYPE")
@Entity
@Data
public class PriceDisplayType implements Serializable {

  private static final long serialVersionUID = 8634208923461686385L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name="DESC_CODE")
  private String descCode;

  private String type;

  private String description;
}
