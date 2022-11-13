package com.sagag.eshop.repo.criteria.user_history;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVehicleHistorySearchCriteria implements Serializable {

  private static final long serialVersionUID = -5012270344761797445L;

  private Integer orgId;

  private Long userId;

  private String fullName;

  private String vehicleName;

  private String vehicleClass;

  private String searchTerm;

  private Date fromDate;

  private Date toDate;

  private String filterMode;

  private Boolean orderDescByVehicleName;

  private Boolean orderDescBySearchTerm;

  private Boolean orderDescBySelectDate;

  private Boolean orderDescByFullName;
}
