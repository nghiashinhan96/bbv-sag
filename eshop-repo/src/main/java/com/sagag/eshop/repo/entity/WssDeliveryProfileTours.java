package com.sagag.eshop.repo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sagag.services.common.enums.WssDeliveryProfileDay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "WSS_DELIVERY_PROFILE_TOURS")
public class WssDeliveryProfileTours implements Serializable {

  private static final long serialVersionUID = 3143099931812029801L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "SUPPLIER_DEPARTURE_TIME")
  private Time supplierDepartureTime;

  @Column(name = "IS_OVER_NIGHT")
  @NotNull(message = "isOverNight is null")
  private boolean isOverNight;

  @Column(name = "PICKUP_WAIT_DURATION")
  private Integer pickupWaitDuration;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "SUPPLIER_TOUR_DAY")
  private WssDeliveryProfileDay supplierTourDay;

  @ManyToOne
  @JoinColumn(name = "WSS_DELIVERY_PROFILE_ID")
  @JsonBackReference
  private WssDeliveryProfile wssDeliveryProfile;

  @ManyToOne
  @JoinColumn(name = "WSS_TOUR_ID")
  @JsonBackReference
  private WssTour wssTour;

}
