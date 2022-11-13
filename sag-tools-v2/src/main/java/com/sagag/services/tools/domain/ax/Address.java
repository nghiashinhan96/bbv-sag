package com.sagag.services.tools.domain.ax;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

import org.springframework.hateoas.Link;

/**
 * <p>
 * The data transfer object class of address info from external services.
 * </p>
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
      "id",
      "salutationCode",
      "salutation",
      "salutationDesc",
      "keyword",
      "surname",
      "name",
      "companyName",
      "street",
      "postOfficeBox",
      "section",
      "addon",
      "active",
      "postCode",
      "city",
      "countryCode",
      "country",
      "countryDesc",
      "stateCode",
      "state",
      "stateDesc",
      "addressTypeCode",
      "addressType",
      "addressTypeDesc",
      "addressId",
      "streetNumber",
      "fullAddress",
      "primary",
      "links"
    })
public class Address implements Serializable {

  private static final long serialVersionUID = 6313948434212906956L;

  private static final String NEXT_TOUR_KEY = "next-tour";

  private String id;

  private String salutationCode;

  private String salutation;

  private String salutationDesc;

  private String keyword;

  private String surname;

  private String name;

  private String companyName;

  private String street;

  private String postOfficeBox;

  private String section;

  private String addon;

  private Boolean active;

  private String postCode;

  private String city;

  private String countryCode;

  private String country;

  private String countryDesc;

  private String stateCode;

  private String state;

  private String stateDesc;

  private String addressTypeCode;

  private String addressType;

  private String addressTypeDesc;

  private String addressId;

  private String streetNumber;

  private boolean primary;

  @JsonProperty("_links")
  private Map<String, Link> links;

  @JsonIgnore
  public String getAddressNextToursUrl() {
    return links.get(NEXT_TOUR_KEY).getHref();
  }

}
