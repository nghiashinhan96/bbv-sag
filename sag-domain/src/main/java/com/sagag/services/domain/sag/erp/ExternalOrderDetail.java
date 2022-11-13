package com.sagag.services.domain.sag.erp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.hateoas.Link;

import java.io.Serializable;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "nr",
    "typeCode",
    "type",
    "typeDesc",
    "sendMethodCode",
    "sendMethod",
    "sendMethodDesc",
    "statusCode",
    "status",
    "statusDesc",
    "partialDelivery",
    "partialInvoice",
    "date",
    "links" })
public class ExternalOrderDetail implements Serializable {

  private static final long serialVersionUID = -6998389434446948018L;

  public static final String SELF = "self";

  public static final String DELIVERY_ADDRESS = "delivery-address";

  public static final String COLLECION_POSITIONS = "collection/positions";

  private String nr;
  private String typeCode;
  private String type;
  private String typeDesc;
  private String sendMethodCode;
  private String sendMethod;
  private String sendMethodDesc;
  private String statusCode;
  private String status;
  private String statusDesc;
  private Boolean partialDelivery;
  private Boolean partialInvoice;
  private String date;

  @JsonProperty("_links")
  private Map<String, Link> links;
}
