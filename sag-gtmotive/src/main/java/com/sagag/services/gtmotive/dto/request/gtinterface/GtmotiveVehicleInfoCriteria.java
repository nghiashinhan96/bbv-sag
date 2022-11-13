package com.sagag.services.gtmotive.dto.request.gtinterface;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.gtmotive.domain.GtmotiveProfileDto;
import com.sagag.services.gtmotive.domain.request.EstimateIdOperationMode;
import com.sagag.services.gtmotive.domain.request.GtmotiveRequestMode;
import com.sagag.services.gtmotive.domain.request.GtmotiveVinDecodeCriteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Locale;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class GtmotiveVehicleInfoCriteria extends GtmotiveVinDecodeCriteria implements Serializable {

  private static final long serialVersionUID = 9145745854128141413L;

  private String estimateId;

  private String kilometers;

  private String vehicle;

  private String userId;

  private String gsId;

  private String gsPwd;

  private String customerId;

  private String umc;

  private String registrationNumber;

  private String gtMod;

  private String gtEng;

  private String gtDrv;

  private Locale locale;

  private GtmotiveRequestMode requestMode;

  // #1066: Use for keep typenschein search term
  private String vehicleCode;

  private EstimateIdOperationMode operation;

  public void bindGtmotiveProfile(GtmotiveProfileDto profile) {
    setCustomerId(profile.getClientid());
    setGsId(profile.getGsId());
    setGsPwd(profile.getPassword());
    setUserId(profile.getUserid());
  }

  @JsonIgnore
  public boolean hasVin() {
    return !StringUtils.isBlank(getModifiedVin());
  }

  @JsonIgnore
  public boolean hasEstimateId() {
    return !StringUtils.isBlank(estimateId);
  }

  @JsonIgnore
  public boolean isVinRequest() {
    return requestMode != null && requestMode.isVin() && hasVin();
  }

}
