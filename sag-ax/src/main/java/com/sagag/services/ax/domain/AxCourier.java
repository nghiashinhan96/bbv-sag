package com.sagag.services.ax.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.domain.sag.external.Courier;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Class to receive the courier info from Dynamic AX ERP.
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AxCourier extends AxBaseResponse {

  private static final long serialVersionUID = 2265124094137904073L;

  private String courierServiceCode;

  private String description;

  @JsonIgnore
  public Courier toCourierDto() {
    return Courier.builder().courierServiceCode(courierServiceCode).description(description)
        .build();
  }

}
