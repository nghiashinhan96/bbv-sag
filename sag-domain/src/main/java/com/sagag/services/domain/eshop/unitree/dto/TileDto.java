package com.sagag.services.domain.eshop.unitree.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({ "id", "tileNodeId", "tileExternals", "tileImage", "tileName", "tileSort",
    "tileNodeLink", "tileType" })
public class TileDto implements Serializable {

  private static final long serialVersionUID = 8361513310811592383L;

  private String id;

  private String tileNodeId;

  private List<TileExternalDto> tileExternals;

  private String tileImage;

  private String tileName;

  private String tileSort;

  private String tileNodeLink;

  private String tileType;

  public TileDto() {
    this.tileExternals = new ArrayList<>();
  }
}
