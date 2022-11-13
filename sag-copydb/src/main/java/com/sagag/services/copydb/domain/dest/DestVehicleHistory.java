package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the VEHICLE_HISTORY database table.
 * 
 */
@Entity
@Table(name = "VEHICLE_HISTORY")
@NamedQuery(name = "DestVehicleHistory.findAll", query = "SELECT v FROM DestVehicleHistory v")
@Data
public class DestVehicleHistory implements Serializable {

  private static final long serialVersionUID = 1120655331043412582L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "ID_SAG")
  private String idSag;

  @Column(name = "ID_SAG_MAKE")
  private String idSagMake;

  @Column(name = "ID_SAG_MODEL")
  private String idSagModel;

  @Column(name = "VEH_ID")
  private String vehId;

  @Column(name = "VEH_INFO")
  private String vehInfo;

  @Column(name = "VEH_MAKE")
  private String vehMake;

  @Column(name = "VEH_MODEL")
  private String vehModel;

  @Column(name = "VEH_NAME")
  private String vehName;

  @Column(name = "VEH_TYPE")
  private String vehType;

  @Column(name = "VEH_VIN")
  private String vehVin;

}
