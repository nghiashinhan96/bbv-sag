package com.sagag.services.tools.domain.target;

import lombok.Data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "TOUR_TIME")
//@Table(name = "TOUR_TIME_SWAP")
public class TargetTourTime implements Serializable {

  private static final long serialVersionUID = 856825066933617228L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "CUSTOMER_NUMBER")
  private String customerNumber;

  @Column(name = "BRANCH_ID")
  private String branchId;

  @Column(name = "TOUR_NAME")
  private String tourName;
}
