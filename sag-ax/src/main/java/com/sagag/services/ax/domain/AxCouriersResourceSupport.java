package com.sagag.services.ax.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;
import java.util.List;

/**
 * Class to receive the list of couriers from Dynamic AX ERP.
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AxCouriersResourceSupport extends ResourceSupport implements Serializable {

  private static final long serialVersionUID = -1093107346017983806L;

  private List<AxCourier> courierServices;

  @JsonIgnore
  public boolean hasCourierss() {
    return CollectionUtils.isNotEmpty(courierServices);
  }

}
