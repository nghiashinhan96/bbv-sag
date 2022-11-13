package com.sagag.eshop.repo.entity.user_history;

import com.sagag.services.common.enums.UserHistoryFromSource;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "V_USER_VEHICLE_HISTORY")
@Data
public class VUserVehicleHistory implements Serializable {

  private static final long serialVersionUID = 6340610859646247884L;

  @Id
  private Long id;

  private Long userId;

  private String userName;

  private String firstName;

  private String lastName;

  private String fullName;

  private Integer orgId;

  private String orgCode;

  private String vehicleId;

  private String vehicleName;

  private String vehicleClass;

  private String searchTerm;

  private String searchMode;

  @Enumerated(EnumType.STRING)
  private UserHistoryFromSource fromSource;

  private Date selectDate;

}
