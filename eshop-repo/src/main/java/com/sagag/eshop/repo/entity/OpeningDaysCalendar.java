package com.sagag.eshop.repo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Date;

@Entity
@NamedQuery(name = "OpeningDaysCalendar.findAll", query = "SELECT o FROM OpeningDaysCalendar o")
@Data
@Builder
@Table(name = "OPENING_DAYS_CALENDAR")
@NoArgsConstructor
@AllArgsConstructor
public class OpeningDaysCalendar implements Serializable {

  private static final long serialVersionUID = 6353100466100810921L;

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
  @JoinColumn(name = "WORKING_DAY_ID")
  @JsonBackReference
  private WorkingDay workingDay;

  @Column(name = "EXCEPTIONS")
  private String exceptions;

}
