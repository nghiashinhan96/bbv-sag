package com.sagag.eshop.repo.entity;

import com.sagag.services.common.enums.ExternalApp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EXTERNAL_ORGANISATION")
public class ExternalOrganisation implements Serializable {

  private static final long serialVersionUID = 2061117131880931660L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Integer orgId;

  private String externalCustomerId;

  private String externalCustomerName;

  @Enumerated(EnumType.STRING)
  @Column(name = "EXTERNAL_APP")
  private ExternalApp externalApp;

}
