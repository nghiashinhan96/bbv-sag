package com.sagag.services.domain.eshop.unitree.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;

@Data
@JsonPropertyOrder({ "tileLink", "tileType", "tileLinkAttr", "tileLinkSort", "tileLinkText",
    "tileLinkType", "tileLinkHighlighted" })
public class TileExternalDto implements Serializable {

  private static final long serialVersionUID = -6141257981784146474L;

  private String tileLink;

  private String tileType;

  private String tileLinkAttr;

  private String tileLinkSort;

  private String tileLinkText;

  private String tileLinkType;

  private String tileLinkHighlighted;

  private boolean activeLink = true;
}
