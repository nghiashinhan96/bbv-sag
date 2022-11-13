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
 * The persistent class for the EXTERNAL_USER database table.
 * 
 */
@Entity
@Table(name = "EXTERNAL_USER")
@NamedQuery(name = "ExternalUser.findAll", query = "SELECT e FROM ExternalUser e")
@Data
public class ExternalUser implements Serializable {

  private static final long serialVersionUID = -693930253219879934L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "ACTIVE")
  private Boolean active;

  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "ESHOP_USER_ID")
  private Long eshopUserId;

  @Column(name = "EXTERNAL_APP")
  private String externalApp;

  @Column(name = "LOCK_VIRTUAL_USER")
  private String lockVirtualUser;

  @Column(name = "PASSWORD")
  private String password;

  @Column(name = "USERNAME")
  private String username;

}