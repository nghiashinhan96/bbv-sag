package com.sagag.services.ax.domain;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import org.springframework.hateoas.Link;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.ax.enums.AxOrderStatus;
import com.sagag.services.domain.sag.erp.ExternalOrderDetail;

import lombok.Data;

/**
 * Class to receive Ax order detail response from Dynamic AX ERP.
 *
 */
@Data
public class AxOrderDetail implements Serializable {

  private static final long serialVersionUID = -6998389434446948018L;

  public static final String SELF = "self";

  public static final String DELIVERY_ADDRESS = "delivery-address";

  public static final String COLLECION_POSITIONS = "collection/positions";

  private String nr;

  private String type;

  private String typeDesc;

  private String sendMethod;

  private AxOrderStatus status;

  private String date;

  @JsonProperty("_links")
  private Map<String, Link> links;

  @JsonIgnore
  public ExternalOrderDetail toExternalOrderDetailDto() {
    return ExternalOrderDetail.builder()
        .nr(this.nr).type(this.type).typeDesc(this.typeDesc)
        .sendMethod(this.sendMethod)
        .status(Objects.isNull(this.status) ? null : this.status.name())
        .statusCode(Objects.isNull(this.status) ? null : this.status.name())
        .date(this.date)
        .links(this.links).build();
  }
}
