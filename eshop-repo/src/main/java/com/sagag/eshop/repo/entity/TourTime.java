package com.sagag.eshop.repo.entity;

import lombok.Data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity of TourTime table.
 *
 */
@Data
@Entity
@Table(name = "TOUR_TIME")
public class TourTime implements Serializable {

  private static final long serialVersionUID = 856825066933617228L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String customerNumber;

  private String branchId;

  private String tourName;

  private String tourDays;

  private String tourDepartureTime;

  private Integer cutOffMinutes;

}
