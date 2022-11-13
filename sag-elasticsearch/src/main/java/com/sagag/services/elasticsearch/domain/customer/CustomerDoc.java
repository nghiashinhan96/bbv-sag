package com.sagag.services.elasticsearch.domain.customer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Id;

/**
 * Elasticsearch Customer Document class.
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
// @formatter:off
@Document(
      indexName = "customers_at",
      type = "customer",
      shards = 5,
      replicas = 1,
      refreshInterval = "-1",
      createIndex = false,
      useServerConfiguration = true
    )
//@formatter:on
public class CustomerDoc implements Serializable {

  private static final long serialVersionUID = -4613181128791300592L;

  @Id
  @JsonProperty("id")
  private String id;

  @JsonProperty("tax_exempt_code")
  private String taxExemptCode;

  @JsonProperty("primary_fax")
  private String primaryFax;

  @JsonProperty("customer_account_nr")
  private String customerAccountNr;

  @JsonProperty("mandant")
  private String mandant;

  @JsonProperty("offical_registration_code")
  private Integer officalRegistrationCode;

  @JsonProperty("primary_fax_cc")
  private String primaryFaxCc;

  @JsonProperty("tax_nr")
  private String taxNr;

  @JsonProperty("addresses")
  private List<CustomerAddress> addresses;

  @JsonProperty("name_alias")
  private String nameAlias;

  @JsonProperty("primary_email")
  private String primaryEmail;

  @JsonProperty("contacts")
  private List<CustomerContact> contacts;

  @JsonProperty("country_code")
  private String countryCode;

  @JsonProperty("primary_phone")
  private String primaryPhone;

  @JsonProperty("primary_phone_cc")
  private String primaryPhoneCc;

  @JsonProperty("customer_name")
  private String customerName;

  @JsonProperty("disposalNumber")
  private String disposalNumber;

  @JsonProperty("is_shop_customer")
  private Boolean isShopCustomer;
}
