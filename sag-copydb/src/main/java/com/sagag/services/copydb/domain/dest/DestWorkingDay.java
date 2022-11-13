package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the WORKING_DAY database table.
 * 
 */
@Entity
@Table(name = "WORKING_DAY")
@NamedQuery(name = "DestWorkingDay.findAll", query = "SELECT w FROM DestWorkingDay w")
@Data
public class DestWorkingDay implements Serializable {

  private static final long serialVersionUID = -7092147534411926185L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "CODE")
  private String code;

  @Column(name = "DESCRIPTION")
  private String description;

}
