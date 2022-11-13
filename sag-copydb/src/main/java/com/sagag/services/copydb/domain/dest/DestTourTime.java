package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the TOUR_TIME database table.
 * 
 */
@Entity
@Table(name = "TOUR_TIME")
@NamedQuery(name = "DestTourTime.findAll", query = "SELECT t FROM DestTourTime t")
@Data
public class DestTourTime implements Serializable {

  private static final long serialVersionUID = -7542718003992275440L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "BRANCH_ID")
  private String branchId;

  @Column(name = "CUSTOMER_NUMBER")
  private String customerNumber;

  @Column(name = "TOUR_NAME")
  private String tourName;

}
