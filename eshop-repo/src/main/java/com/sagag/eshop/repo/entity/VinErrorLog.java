package com.sagag.eshop.repo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "VIN_ERROR_LOG")
@NamedQuery(name = "VinErrorLog.findAll", query = "SELECT r FROM VinErrorLog r")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VinErrorLog implements Serializable {

  private static final long serialVersionUID = -7153013281624353305L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String type;

  private String vin;

  private String cupi;

  private String location;

  private String oeNr;

  private String umc;

  private String returnedData;

  private Long createdUserId;

  private Date createdDate;

}
