package com.sagag.services.tools.domain.sagsys;

import lombok.Data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The persistent class for the AAD_ACCOUNTS database table.
 * 
 */
@Entity
@Table(name = "AAD_ACCOUNTS")
@Data
public class AadAccounts implements Serializable {

  private static final long serialVersionUID = 767833942430624609L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "FIRST_NAME")
  private String firstName;

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
