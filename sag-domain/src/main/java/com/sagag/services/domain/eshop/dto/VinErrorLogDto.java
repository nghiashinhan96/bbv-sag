package com.sagag.services.domain.eshop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * VIN error log Dto class.
 */
@Data
@Builder
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
@AllArgsConstructor
public class VinErrorLogDto implements Serializable {

  private static final long serialVersionUID = 5615564779449772993L;

  private String type;

  private String vin;

  private String cupi;

  private String location;

  private String oeNr;

  private String umc;

  private String returnedData;

  private Long createdUserId;

  private Date createdDate;

  public VinErrorLogDto(String vin, String cupi, String location) {
    this.vin = vin;
    this.cupi = cupi;
    this.location = location;
  }

  public VinErrorLogDto(String vin) {
    this.vin = vin;
    this.createdDate = new Date();
  }

}
