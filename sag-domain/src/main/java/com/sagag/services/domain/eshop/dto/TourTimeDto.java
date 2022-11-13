package com.sagag.services.domain.eshop.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TourTimeDto implements Serializable {

  private static final long serialVersionUID = 3074888395574296604L;

  private Integer id;

  private String customerNumber;

  private String branchId;

  private String branchName;

  private String tourName;

  private String tourDays;

  private String tourDepartureTime;

  private Integer cutOffMinutes;
}
