package com.sagag.services.domain.eshop.dto;

import com.sagag.services.domain.sag.external.Courier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourierDto implements Serializable {

  private static final long serialVersionUID = 5287181915670020100L;

  private String courierServiceCode;

  private String description;

  public CourierDto(Courier courier) {
    this.courierServiceCode = courier.getCourierServiceCode();
    this.description = courier.getDescription();
  }

}
