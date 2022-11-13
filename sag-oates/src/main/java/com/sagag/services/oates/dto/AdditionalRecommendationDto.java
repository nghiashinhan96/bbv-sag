package com.sagag.services.oates.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalRecommendationDto {

  @JsonProperty("@created")
  private Date created;

  private List<RecommendProductDto> products;
}
