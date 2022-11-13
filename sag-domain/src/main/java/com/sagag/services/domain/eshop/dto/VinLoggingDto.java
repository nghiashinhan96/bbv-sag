package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VinLoggingDto implements Serializable {

  private static final long serialVersionUID = 2369736726021677553L;

  private Integer id;

  private String vin;

  private Long customerID;

  private String estimateID;

  private String vehicleID;

  private Date dateOfLogEntry;

  private Integer status;

  private Integer errorCode;

  public static VinLoggingDto buildLogInfo(final Long custNr, String estimateId, String vin,
    final String vehicleId) {
    VinLoggingDto vinLogDto = new VinLoggingDto();
    vinLogDto.setEstimateID(estimateId);
    vinLogDto.setCustomerID(custNr);
    vinLogDto.setVin(vin);
    vinLogDto.setVehicleID(vehicleId);
    vinLogDto.setDateOfLogEntry(Calendar.getInstance().getTime());
    return vinLogDto;
  }

}
