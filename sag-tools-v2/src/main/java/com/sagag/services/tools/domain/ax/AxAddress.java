package com.sagag.services.tools.domain.ax;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sagag.services.tools.domain.external.CustomLink;
import com.sagag.services.tools.support.CustomerAddressType;

import lombok.Data;

/**
 * Class to receive Ax customer address from Dynamic AX ERP.
 *
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
    {
      "id",
      "street",
      "streetNumber",
      "postOfficeBox",
      "postCode",
      "city",
      "countryCode",
      "country",
      "state",
      "active",
      "primary",
      "addressType",
      "_links"
    })
public class AxAddress implements Serializable {

  private static final long serialVersionUID = 6313948434212906956L;

  private String id;

  private String street;

  private String streetNumber;

  private String postOfficeBox;

  private String postCode;

  private String city;

  private String countryCode;

  private String country;

  private String state;

  private boolean active;

  private boolean primary;

  private CustomerAddressType addressType;

  @JsonProperty("_links")
  private Map<String, CustomLink> links;

  @JsonIgnore
  public Address toAddressDto() {
    return Address.builder()
        .id(this.id).addressId(this.id).street(this.street).streetNumber(this.streetNumber)
        .postOfficeBox(this.postOfficeBox).postCode(this.postCode)
        .city(this.city).countryCode(this.countryCode).country(this.country).state(this.state)
        .addressType(this.addressType.name()).addressTypeCode(this.addressType.name())
        .active(this.active).primary(this.primary).build();
  }

}
