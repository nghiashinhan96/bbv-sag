package com.sagag.services.ivds.response;

import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.elasticsearch.dto.VehicleSearchResponse;
import com.sagag.services.ivds.converter.VehicleConverters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleSearchResponseDto implements Serializable {

  private static final long serialVersionUID = 9156403581525121641L;

  private Page<VehicleDto> vehicles;

  private Map<String, List<Object>> aggregations;

  public static VehicleSearchResponseDto map(VehicleSearchResponse response) {
    return VehicleSearchResponseDto.builder()
        .vehicles(response.getVehicles().map(VehicleConverters.simpleVehicleConverter()))
        .aggregations(response.getAggregations())
        .build();
  }
}
