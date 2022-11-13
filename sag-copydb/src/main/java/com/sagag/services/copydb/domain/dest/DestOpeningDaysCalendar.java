package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the OPENING_DAYS_CALENDAR database table.
 * 
 */
@Entity
@Table(name = "OPENING_DAYS_CALENDAR")
@NamedQuery(name = "DestOpeningDaysCalendar.findAll", query = "SELECT o FROM DestOpeningDaysCalendar o")
@Data
public class DestOpeningDaysCalendar implements Serializable {

  private static final long serialVersionUID = 4713194834409544535L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "COUNTRY_ID")
  private Integer countryId;

  @Column(name = "DATETIME")
  private Date datetime;

  @Column(name = "[EXCEPTIONS]")
  private String exceptions;

  @Column(name = "WORKING_DAY_ID")
  private Integer workingDayId;

}
