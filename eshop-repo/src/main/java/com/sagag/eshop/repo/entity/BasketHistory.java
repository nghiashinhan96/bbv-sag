package com.sagag.eshop.repo.entity;

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
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BASKET_HISTORY")
public class BasketHistory implements Serializable {

  private static final long serialVersionUID = 411776069941118516L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String basketName;

  private Integer organisationId;

  private Long createdUserId;

  private Long salesUserId;

  private Double grandTotalExcludeVat;

  @Lob
  private String basketJson;

  private Date updatedDate;

  @Column(name = "ACTIVE")
  private boolean active;

  private String customerRefText;

}
