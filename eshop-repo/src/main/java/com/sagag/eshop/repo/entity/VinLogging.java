package com.sagag.eshop.repo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity class for table VIN_LOGGING mappings.
 */
@Table(name = "VIN_LOGGING")
@Entity
@NamedQuery(name = "VinLogging.findAll", query = "SELECT v FROM VinLogging v")
@Data
public class VinLogging implements Serializable {

  private static final long serialVersionUID = 2369736726021677553L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "VIN")
  private String vin;

  @Column(name = "CUSTOMER_ID")
  private Long customerID;

  @Column(name = "ESTIMATE_ID")
  private String estimateID;

  @Column(name = "VEHICLE_ID")
  private String vehicleID;

  @Basic(optional = false)
  @Column(name = "DATE_OF_LOG_ENTRY", insertable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date dateOfLogEntry;

  @Column(name = "STATUS")
  private Integer status;

  @Column(name = "ERROR_CODE")
  private Integer errorCode;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "SALES_USER_ID")
  private Long salesUserId;

}
