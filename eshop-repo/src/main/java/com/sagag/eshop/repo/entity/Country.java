package com.sagag.eshop.repo.entity;

import lombok.Data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity of Country table.
 *
 */
@Data
@Entity
@Table(name = "COUNTRY")
public class Country implements Serializable {

  private static final long serialVersionUID = 75512829882956338L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String code;

  @Column(name = "SHORT_NAME")
  private String shortName;

  @Column(name = "FULL_NAME")
  private String fullName;

  @Column(name = "SHORT_CODE")
  private String shortCode;
}
