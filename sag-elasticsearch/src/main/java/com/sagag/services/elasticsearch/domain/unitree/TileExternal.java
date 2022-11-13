package com.sagag.services.elasticsearch.domain.unitree;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;

@Data
//@formatter:off
@JsonPropertyOrder({
		"tile_link",
		"tile_type",
		"tile_link_sort",
		"tile_link_attr",
		"tile_link_type",
		"tile_link_text",
		"tile_link_highlighted"})
//@formatter:on
public class TileExternal implements Serializable {

  private static final long serialVersionUID = 1259101638558266017L;

  @JsonProperty("tile_link")
  private String tileLink;

  @JsonProperty("tile_link_attr")
  private String tileLinkAttr;

  @JsonProperty("tile_type")
  private String tileType;

  @JsonProperty("tile_link_sort")
  private String tileLinkSort;

  @JsonProperty("tile_link_text")
  private String tileLinkText;

  @JsonProperty("tile_link_type")
  private String tileLinkType;

  @JsonProperty("tile_link_highlighted")
  private String tileLinkHighlighted;
}
