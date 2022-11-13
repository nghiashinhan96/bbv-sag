package com.sagag.services.elasticsearch.domain.customer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.io.Serializable;

/**
 * Class Customer contact.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerContact implements Serializable {

  private static final long serialVersionUID = 5890700579368350552L;

  @JsonProperty("last_name")
  private String lastName;
  @JsonProperty("sort")
  private Integer sort;
  @JsonProperty("first_name")
  private String firstName;

}
