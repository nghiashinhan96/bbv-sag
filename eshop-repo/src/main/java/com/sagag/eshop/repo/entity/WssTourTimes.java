package com.sagag.eshop.repo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sagag.services.common.enums.WeekDay;

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

/**
 * Entity of WssTourTimes table.
 *
 */
@Builder
@Data
@Entity
@Table(name = "WSS_TOUR_TIMES")
@NoArgsConstructor
@AllArgsConstructor
public class WssTourTimes implements Serializable {

  private static final long serialVersionUID = -4891687001138677423L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "WEEK_DAY_NO")
  private WeekDay weekDay;

  @Column(name = "DEPARTURE_TIME")
  private Time departureTime;

  @ManyToOne
  @JoinColumn(name = "WSS_TOUR_ID")
  @JsonBackReference
  private WssTour wssTour;

}
