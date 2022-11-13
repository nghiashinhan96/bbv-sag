package com.sagag.services.elasticsearch.domain.unitree;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import org.dozer.Mapping;

import java.io.Serializable;
import java.util.List;

@Data
//@formatter:off
@JsonPropertyOrder({
  "tile_id",
  "tile_name",
  "tile_sort",
  "tile_image",
  "tile_node_link",
  "tile_node_id",
  "tile_type",
  "tile_external",
})
//@formatter:on
public class Tile implements Serializable {

  private static final long serialVersionUID = 3545871876080593523L;

  @JsonProperty("tile_id")
  private String id;

  @JsonProperty("tile_name")
  private String tileName;

  @JsonProperty("tile_sort")
  private String tileSort;

  @JsonProperty("tile_image")
  private String tileImage;

  @JsonProperty("tile_node_link")
  private String tileNodeLink;

  @JsonProperty("tile_node_id")
  private String tileNodeId;

  @JsonProperty("tile_type")
  private String tileType;

  @Mapping("tileExternals")
  @JsonProperty("tile_external")
  private List<TileExternal> tileExternals;
}
