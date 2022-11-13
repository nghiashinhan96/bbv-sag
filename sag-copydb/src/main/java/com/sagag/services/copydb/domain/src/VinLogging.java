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
 * The persistent class for the VIN_LOGGING database table.
 * 
 */
@Entity
@Table(name = "VIN_LOGGING")
@NamedQuery(name = "VinLogging.findAll", query = "SELECT v FROM VinLogging v")
@Data
public class VinLogging implements Serializable {

  private static final long serialVersionUID = -7064784391641476025L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "CUSTOMER_ID")
  private Long customerId;

  @Column(name = "DATE_OF_LOG_ENTRY")
  private Date dateOfLogEntry;

  @Column(name = "ERROR_CODE")
  private Integer errorCode;

  @Column(name = "ESTIMATE_ID")
  private String estimateId;

  @Column(name = "STATUS")
  private Integer status;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "VEHICLE_ID")
  private String vehicleId;

  @Column(name = "VIN")
  private String vin;

}
