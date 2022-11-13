package com.sagag.services.elasticsearch.query.vehicles;

import com.sagag.services.elasticsearch.domain.vehicle.VehicleDoc;
import com.sagag.services.elasticsearch.dto.FreetextVehicleDto;
import com.sagag.services.elasticsearch.dto.FreetextVehicleResponse;
import com.sagag.services.elasticsearch.dto.VehicleSearchResponse;
import com.sagag.services.elasticsearch.extractor.ResultsExtractorUtils;

import lombok.experimental.UtilityClass;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ResultsExtractor;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class VehiclesResultExtractors {

  public ResultsExtractor<Optional<FreetextVehicleResponse>> extractByFreetext(Pageable pageable) {
    return response -> {
      final Page<VehicleDoc> vehicleDocs =
          ResultsExtractorUtils.extractPageResult(response, VehicleDoc.class, pageable);
      if (!vehicleDocs.hasContent()) {
        return Optional.empty();
      }
      final FreetextVehicleResponse vehResponse = new FreetextVehicleResponse();
      vehResponse.setVehicles(convertToVehicleDtos(vehicleDocs));
      vehResponse
          .setAggregations(ResultsExtractorUtils.extractAggregations(response.getAggregations()));
      return Optional.of(vehResponse);
    };
  }

  private Page<FreetextVehicleDto> convertToVehicleDtos(final Page<VehicleDoc> vehicleDocs) {
    final List<FreetextVehicleDto> vehicles = vehicleDocs.getContent().parallelStream()
        .map(FreetextVehicleDto::copy).collect(Collectors.toList());
    return new PageImpl<>(vehicles, vehicleDocs.getPageable(), vehicleDocs.getTotalElements());
  }

  public ResultsExtractor<VehicleSearchResponse> extractByFiltering(Pageable pageable) {
    return response -> {
      final Page<VehicleDoc> vehicleDocs =
          ResultsExtractorUtils.extractPageResult(response, VehicleDoc.class, pageable);
      if (!vehicleDocs.hasContent()) {
        return VehicleSearchResponse.builder().vehicles(Page.empty())
            .aggregations(Collections.emptyMap()).build();
      }
      final VehicleSearchResponse result = new VehicleSearchResponse();
      result.setVehicles(
          new PageImpl<>(vehicleDocs.getContent(), pageable, vehicleDocs.getTotalElements()));
      final Map<String, List<Object>> extractedAggregations =
          ResultsExtractorUtils.extractAggregations(response.getAggregations());

      // Lowercase key map
      result.setAggregations(extractedAggregations.entrySet().stream()
          .collect(Collectors.toMap(agg -> agg.getKey().toLowerCase(), Entry::getValue)));
      return result;
    };
  }
}
