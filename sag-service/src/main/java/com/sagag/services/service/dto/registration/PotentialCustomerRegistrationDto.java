package com.sagag.services.service.dto.registration;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class PotentialCustomerRegistrationDto implements Serializable {

  private static final long serialVersionUID = 5024233450914763853L;

  private String collectionShortName;

  private String langCode;

  private List<PotentialCustomerRegistrationField> fields;

  @JsonIgnore
  public String getContent() {
    return PotentialCustomerRegistrationBuider.buildHtml(this);
  }
}
