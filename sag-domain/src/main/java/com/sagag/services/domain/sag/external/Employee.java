package com.sagag.services.domain.sag.external;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "abbreviation", "name", "contacts"})
public class Employee implements Serializable {

  private static final long serialVersionUID = 5226672744217767697L;

  private String id;

  private String abbreviation;

  private String name;

  private List<ContactInfo> contacts;

}
