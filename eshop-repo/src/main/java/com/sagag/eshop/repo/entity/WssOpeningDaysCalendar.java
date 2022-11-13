package com.sagag.eshop.repo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQuery(name = "WssOpeningDaysCalendar.findAll", query = "SELECT w FROM WssOpeningDaysCalendar w")
@Data
@Builder
@Table(name = "WSS_OPENING_DAYS_CALENDAR")
@NoArgsConstructor
@AllArgsConstructor
public class WssOpeningDaysCalendar implements Serializable {

  private static final long serialVersionUID = -1885216111015403608L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "DATETIME", nullable = false)
  private Date datetime;

  @ManyToOne
  @JoinColumn(name = "COUNTRY_ID")
  @JsonBackReference
  private Country country;

  @ManyToOne
  @JoinColumn(name = "WSS_WORKING_DAY_ID")
  @JsonBackReference
  private WssWorkingDay wssWorkingDay;

  @Column(name = "EXCEPTIONS")
  private String exceptions;

  @Column(name = "ORG_ID")
  private Integer orgId;

}
