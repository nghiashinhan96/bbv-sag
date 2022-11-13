package com.sagag.eshop.repo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "VEHICLE_HISTORY")
@NamedQueries(value = {
    @NamedQuery(name = "VehicleHistory.findAll", query = "SELECT v FROM VehicleHistory v"),
    @NamedQuery(name = "VehicleHistory.findByCode",
    query = "SELECT v FROM VehicleHistory v WHERE v.vehId = :vehid") })
public class VehicleHistory implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private long id;

  @Column(name = "VEH_ID")
  private String vehId;

  @Column(name = "VEH_MAKE")
  private String vehMake;

  @Column(name = "VEH_MODEL")
  private String vehModel;

  @Column(name = "VEH_TYPE")
  private String vehType;

  @Column(name = "VEH_INFO")
  private String vehInfo;

  @Column(name = "VEH_NAME")
  private String vehName;

  @Column(name = "VEH_CLASS")
  private String vehClass;

  @OneToMany(mappedBy = "vehicleHistory")
  @JsonManagedReference
  private List<UserVehicleHistory> userVehicleHistory;

}
