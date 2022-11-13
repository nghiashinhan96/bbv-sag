package com.sagag.services.domain.eshop.dto.externalvendor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class SupportBrandDto implements Serializable {

  private static final long serialVersionUID = -67897599109013103L;
  
  private String dlnrid;

  @JsonProperty("suppname")
  private String suppname;  
}
