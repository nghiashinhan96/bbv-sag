package com.sagag.services.domain.eshop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleGenArtArtDto implements Serializable {

  private static final long serialVersionUID = 1784175014437447511L;

  @JsonProperty("id")
  private String id;

  @JsonProperty("vegen")
  private String vegen;

  @JsonProperty("vehid")
  private String vehId;

  @JsonProperty("genart")
  private Integer genart;

  @JsonProperty("date_create")
  private Integer dateCreate;

  @JsonProperty("date_modify")
  private Integer dateModify;

  @JsonProperty("articles")
  private List<VehGenArtArticleDto> articles;

}
