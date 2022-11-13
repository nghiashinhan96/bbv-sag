package com.sagag.eshop.repo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sagag.services.common.enums.UserHistoryFromSource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "USER_VEHICLE_HISTORY")
@NamedQueries(value = {
    @NamedQuery(name = "UserVehicleHistory.findAll", query = "SELECT g FROM UserVehicleHistory g"),
    @NamedQuery(name = "UserVehicleHistory.findByUserId",
    query = "SELECT g FROM UserVehicleHistory g WHERE g.userId = :userid ORDER BY g.selectDate DESC"),
    @NamedQuery(name = "UserVehicleHistory.findByUserAndVehHistoryId",
    query = "SELECT g FROM UserVehicleHistory g WHERE g.userId = :userid AND g.vehicleHistory.id = :vehicleHistoryId")})
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString(of = { "id", "vehicleHistory" })
public class UserVehicleHistory implements Serializable {

  private static final long serialVersionUID = -6792470418465679450L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne
  @JsonBackReference
  @JoinColumn(name = "VEH_HISTORY_ID")
  private VehicleHistory vehicleHistory;

  @Column(name = "USER_ID")
  private long userId;

  @Column(name = "SELECT_DATE")
  private Date selectDate;

  @Column(name = "SEARCH_TERM")
  private String searchTerm;

  @Column(name = "SEARCH_MODE")
  private String searchMode;

  @Column(name = "FROM_SOURCE")
  @Enumerated(EnumType.STRING)
  private UserHistoryFromSource fromSource;

}
