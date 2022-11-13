package com.sagag.services.ivds.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.gtmotive.domain.response.GtmotiveSimpleOperation;
import com.sagag.services.ivds.request.vehicle.GTmotiveDirectMatches;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@JsonInclude(Include.NON_NULL)
public class GtmotiveResponse {

  private Page<GTmotiveArticleDocDto> articles;
  
  private Page<GTmotiveDirectMatches> directMatches;

  private GtmotiveCupisResponse cupisResponse;

  private VehicleDto vehicle;

  private List<GtmotiveSimpleOperation> nonMatchedOperations;

  private boolean normauto;

  private String mailContent;

  @JsonIgnore
  public String getVehicleId() {
    if (vehicle == null) {
      return StringUtils.EMPTY;
    }
    return vehicle.getVehId();
  }

}
