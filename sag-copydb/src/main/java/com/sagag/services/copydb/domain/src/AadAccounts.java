package com.sagag.services.copydb.domain.src;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the AAD_ACCOUNTS database table.
 * 
 */
@Entity
@Table(name = "AAD_ACCOUNTS")
@NamedQuery(name = "AadAccounts.findAll", query = "SELECT a FROM AadAccounts a")
@Data
public class AadAccounts implements Serializable {

  private static final long serialVersionUID = 767833942430624609L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "FIRST_NAME")
  private String firstName;

  @Column(name = "GENDER")
  private String gender;

  @Column(name = "LAST_NAME")
  private String lastName;

  @Column(name = "LEGAL_ENTITY_ID")
  private String legalEntityId;

  @Column(name = "PERMIT_GROUP")
  private String permitGroup;

  @Column(name = "PERSONAL_NUMBER")
  private String personalNumber;

  @Column(name = "PRIMARY_CONTACT_EMAIL")
  private String primaryContactEmail;

}
