package com.sagag.services.tools.domain.elasticsearch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

import javax.persistence.Id;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
// @formatter:off
@Document(
    indexName = "branches",
    type = "branch"
  )
@JsonPropertyOrder(
    { "id",
      "country_code",
      "zip",
      "address_desc",
      "address_country",
      "address_street",
      "region_id",
      "primary_email",
      "branch_nr",
      "primary_url",
      "primary_fax",
      "address_city",
      "primary_phone",
      "org_id"
    })
//@formatter:on
public class BranchDoc implements Serializable {

  private static final long serialVersionUID = -5121288037846009095L;

  @Id
  @JsonProperty("id")
  private String id;

  @JsonProperty("zip")
  private String zip;

  @JsonProperty("country_code")
  private String countryCode;

  @JsonProperty("address_desc")
  private String addressDesc;

  @JsonProperty("address_country")
  private String addressCountry;

  @JsonProperty("address_street")
  private String addressStreet;

  @JsonProperty("region_id")
  private String regionId;

  @JsonProperty("primary_email")
  private String primaryEmail;

  @JsonProperty("branch_nr")
  private Integer branchNr;

  @JsonProperty("primary_url")
  private String primaryUrl;

  @JsonProperty("primary_fax")
  private String primaryFax;

  @JsonProperty("address_city")
  private String addressCity;

  @JsonProperty("primary_phone")
  private String primaryPhone;

  @JsonProperty("org_id")
  private Integer orgId;
  
  @JsonProperty("id_ksl")
  private Boolean validForKSL;
}
