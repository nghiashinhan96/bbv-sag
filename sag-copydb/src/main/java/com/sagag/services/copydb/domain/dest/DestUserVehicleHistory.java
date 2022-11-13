package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "USER_VEHICLE_HISTORY")
@NamedQuery(name = "DestUserVehicleHistory.findAll", query = "SELECT u FROM DestUserVehicleHistory u")
@Data
public class DestUserVehicleHistory implements Serializable {

  private static final long serialVersionUID = -6792470418465679450L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "VEH_HISTORY_ID")
  private Long vehHistoryId;

  @Column(name = "SELECT_DATE")
  private Date selectDate;

}
