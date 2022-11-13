package com.sagag.eshop.repo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FINAL_CUSTOMER_PROPERTY")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinalCustomerProperty implements Serializable {

  private static final long serialVersionUID = -5012683152894182866L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long orgId;

  @Column(nullable = true)
  private String settingKey;

  @Column(nullable = false)
  private String value;

}
