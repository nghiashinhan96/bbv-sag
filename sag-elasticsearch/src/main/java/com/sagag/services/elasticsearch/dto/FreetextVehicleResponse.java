package com.sagag.services.elasticsearch.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FreetextVehicleResponse implements Serializable {

  private static final long serialVersionUID = -5074529247109971639L;

  private Page<FreetextVehicleDto> vehicles;

  private Map<String, List<Object>> aggregations;

}
