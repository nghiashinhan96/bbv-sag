package com.sagag.eshop.repo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQuery(name = "WssWorkingDay.findAll", query = "SELECT w FROM WssWorkingDay w")
@Data
@Builder
@Table(name = "WSS_WORKING_DAY")
@NoArgsConstructor
@AllArgsConstructor
public class WssWorkingDay implements Serializable {

  private static final long serialVersionUID = 2664708306676349348L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "CODE", nullable = false)
  private String code;

  @Column(name = "DESCRIPTION")
  private String desciption;

}
