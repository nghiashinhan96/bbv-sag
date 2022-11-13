package com.sagag.services.gtmotive.dto.request.mainmoduleservice;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GtmotiveReferenceSearchByPartCodesCriteria implements Serializable {

  private static final long serialVersionUID = 8457276018350215221L;

  private GtmotivePartsThreeSearchRequest gtmotivePartsThreeSearchRequest;

  private boolean isVinMode;

  private boolean isMaintenance;

  @JsonIgnore
  private String custNr;

  @JsonIgnore
  private String companyName;

  @JsonIgnore
  private String affiliateShortName;
}
