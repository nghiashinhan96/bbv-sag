package com.sagag.services.elasticsearch.dto;

import com.sagag.services.elasticsearch.domain.vehicle.VehicleDoc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleSearchResponse {

  private Page<VehicleDoc> vehicles;

  private Map<String, List<Object>> aggregations;

}
