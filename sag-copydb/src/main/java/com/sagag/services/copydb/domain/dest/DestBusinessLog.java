package com.sagag.services.copydb.domain.dest;

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
@NamedQuery(name = "DestBusinessLog.findAll", query = "SELECT b FROM DestBusinessLog b")
@Data
public class DestBusinessLog implements Serializable {

  private static final long serialVersionUID = -5335553877280713136L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "API")
  private String api;

  @Column(name = "BUSINESS_LOG_TYPE")
  private String businessLogType;

  @Column(name = "CUSTOMER_ID")
  private Long customerId;

  @Column(name = "DATE_OF_LOG_ENTRY")
  private Date dateOfLogEntry;

  @Column(name = "REQUEST")
  private String request;

  @Column(name = "RESPONSE")
  private String response;

  @Column(name = "USER_ID")
  private Long userId;

}
