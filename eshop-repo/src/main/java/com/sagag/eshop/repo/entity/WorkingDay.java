package com.sagag.eshop.repo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@NamedQuery(name = "WorkingDay.findAll", query = "SELECT w FROM WorkingDay w")
@Data
@Builder
@Table(name = "WORKING_DAY")
@NoArgsConstructor
@AllArgsConstructor
public class WorkingDay implements Serializable {

  private static final long serialVersionUID = 6353100466100810921L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "CODE", nullable = false)
  private String code;

  @Column(name = "DESCRIPTION")
  private String desciption;

}
