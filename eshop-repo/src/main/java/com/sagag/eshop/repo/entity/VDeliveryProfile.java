package com.sagag.eshop.repo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "V_DELIVERY_PROFILE")
public class VDeliveryProfile {

  @Id
  private Integer id;
  private String country;
  private Integer deliveryProfileId;
  private String distributionBranchCode;
  private String deliveryBranchCode;
  private Integer nextDay;
  private LocalTime vendorCutOffTime;
  private LocalTime lastDelivery;
  private LocalTime latestTime;
  private Integer deliveryDuration;
  private String deliveryProfileName;

}
