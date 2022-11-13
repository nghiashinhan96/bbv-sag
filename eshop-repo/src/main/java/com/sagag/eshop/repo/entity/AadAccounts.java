package com.sagag.eshop.repo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity mapping class to table AAD_ACCOUNTS.
 */
@Table(name = "AAD_ACCOUNTS")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AadAccounts implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String firstName;
  private String lastName;
  private String primaryContactEmail;
  private String personalNumber;
  private String gender;
  private String legalEntityId;
  private String permitGroup;
  private Date createdDate;
  private String uuid;
  private Long userId;
}
