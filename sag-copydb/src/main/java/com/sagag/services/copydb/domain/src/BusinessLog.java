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
 * The persistent class for the BUSINESS_LOG database table.
 * 
 */
@Entity
@Table(name = "BUSINESS_LOG")
@NamedQuery(name = "BusinessLog.findAll", query = "SELECT b FROM BusinessLog b")
@Data
public class BusinessLog implements Serializable {

  private static final long serialVersionUID = -1662564580558762906L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "API")
  private String api;

  @Column(name = "BUSINESS_LOG_TYPE")
  private String businessLogType;

  @Column(name = "CUSTOMER_ID")
  private long customerId;

  @Column(name = "DATE_OF_LOG_ENTRY")
  private Date dateOfLogEntry;

  @Column(name = "REQUEST")
  private String request;

  @Column(name = "RESPONSE")
  private String response;

  @Column(name = "USER_ID")
  private Long userId;

}
