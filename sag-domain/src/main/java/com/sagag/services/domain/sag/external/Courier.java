package com.sagag.services.domain.sag.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Courier implements Serializable {

  private static final long serialVersionUID = 4683263897244565616L;

  private String courierServiceCode;

  private String description;

}
