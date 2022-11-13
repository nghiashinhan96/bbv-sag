package com.sagag.services.oates.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendProductDto implements Serializable {

  private static final long serialVersionUID = -6993341660955794559L;

  private String id;

  private String name;

  @JsonProperty("@external_id")
  private Object externalId;

  @JsonProperty("@external_data")
  private List<Object> externalData;

  private List<ProductResourceDto> resources;
}
