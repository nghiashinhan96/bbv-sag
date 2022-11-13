package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MIGRATION_AX_ADDR database table.
 * 
 */
@Entity
@Table(name = "MIGRATION_AX_ADDR")
@NamedQuery(name = "DestMigrationAxAddr.findAll", query = "SELECT m FROM DestMigrationAxAddr m")
@Data
public class DestMigrationAxAddr implements Serializable {

  private static final long serialVersionUID = 2173857759074265955L;

  @Id
  private long id;
  
  @Column(name = "ADDRESSCITY")
  private String addresscity;

  @Column(name = "ADDRESSCOUNTRYREGIONID")
  private String addresscountryregionid;

  @Column(name = "ADDRESSDESCRIPTION")
  private String addressdescription;

  @Column(name = "ADDRESSLOCATIONID")
  private String addresslocationid;

  @Column(name = "ADDRESSLOCATIONROLES")
  private String addresslocationroles;

  @Column(name = "ADDRESSPOSTBOX")
  private String addresspostbox;

  @Column(name = "ADDRESSSTREET")
  private String addressstreet;

  @Column(name = "ADDRESSZIPCODE")
  private String addresszipcode;

  @Column(name = "BUILDINGCOMPLIMENT")
  private String buildingcompliment;

  @Column(name = "[?CUSTOMERACCOUNTNUMBER]")
  private String _customeraccountnumber;

  @Column(name = "CUSTOMERLEGALENTITYID")
  private String customerlegalentityid;

  @Column(name = "ISLOCATIONOWNER")
  private String islocationowner;

  @Column(name = "ISPOSTALADDRESS")
  private String ispostaladdress;

  @Column(name = "ISPRIMARY")
  private String isprimary;

  @Column(name = "ISPRIVATE")
  private String isprivate;

  @Column(name = "ISPRIVATEPOSTALADDRESS")
  private String isprivatepostaladdress;

  @Column(name = "ISROLEBUSINESS")
  private String isrolebusiness;

  @Column(name = "ISROLEDELIVERY")
  private String isroledelivery;

  @Column(name = "ISROLEHOME")
  private String isrolehome;

  @Column(name = "ISROLEINVOICE")
  private String isroleinvoice;

}
