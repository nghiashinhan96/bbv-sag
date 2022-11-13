package com.sagag.services.copydb.domain.src;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the USER_VEHICLE_HISTORY database table.
 * 
 */
@Entity
@Table(name = "USER_VEHICLE_HISTORY")
@NamedQuery(name = "UserVehicleHistory.findAll", query = "SELECT u FROM UserVehicleHistory u")
@Data
public class UserVehicleHistory implements Serializable {

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
