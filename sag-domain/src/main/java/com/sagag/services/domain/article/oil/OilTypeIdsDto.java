package com.sagag.services.domain.article.oil;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@JsonPropertyOrder({ "id", "name"})
public class OilTypeIdsDto implements Serializable {

  private static final long serialVersionUID = -6635129530994566832L;

  @NonNull
  @JsonProperty("id")
  private String id;

  @NonNull
  @JsonProperty("name")
  private String name;

  private String groupId;

  private String groupName;
}
