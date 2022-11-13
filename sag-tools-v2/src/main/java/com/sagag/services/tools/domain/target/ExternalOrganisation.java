package com.sagag.services.tools.domain.target;


import com.sagag.services.tools.support.ExternalApp;

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

  @Column(name = "ORG_ID")
  private Integer orgId;

  @Column(name = "EXTERNAL_CUSTOMER_ID")
  private String externalCustomerId;

  @Column(name = "EXTERNAL_CUSTOMER_NAME")
  private String externalCustomerName;

  @Enumerated(EnumType.STRING)
  @Column(name = "EXTERNAL_APP")
  private ExternalApp externalApp;

}
