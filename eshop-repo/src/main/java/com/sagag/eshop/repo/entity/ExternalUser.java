package com.sagag.eshop.repo.entity;

import com.sagag.services.common.enums.ExternalApp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "EXTERNAL_USER")
public class ExternalUser implements Serializable {

  private static final long serialVersionUID = -5917021584438153901L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "ESHOP_USER_ID")
  private Long eshopUserId;

  private String username;

  private String password;

  private boolean active;

  private Date createdDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "EXTERNAL_APP")
  private ExternalApp externalApp;

  /**
   * pattern: orgCode_virtualUserId
   */
  private String lockVirtualUser;
}
