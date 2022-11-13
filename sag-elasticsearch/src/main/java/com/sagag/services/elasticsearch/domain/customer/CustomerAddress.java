package com.sagag.services.elasticsearch.domain.customer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.io.Serializable;

/**
 * Class Customer address.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerAddress implements Serializable {

  private static final long serialVersionUID = 5890700579368350552L;

  @JsonProperty("address_desc")
  private String addressDesc;
  @JsonProperty("sort")
  private Integer sort;
  @JsonProperty("id_location")
  private String idLocation;
  @JsonProperty("zip")
  private String zip;
  @JsonProperty("city")
  private String city;
  @JsonProperty("building_comp")
  private String buildingComp;
  @JsonProperty("address_type")
  private String addressType;
  @JsonProperty("address_street")
  private String addressStreet;
  @JsonProperty("is_primary")
  private boolean primary;
}
