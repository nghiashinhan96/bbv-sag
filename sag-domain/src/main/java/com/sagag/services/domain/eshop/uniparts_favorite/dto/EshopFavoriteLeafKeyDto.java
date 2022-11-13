package com.sagag.services.domain.eshop.uniparts_favorite.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "treeId", "leafId", "gaId" })
public class EshopFavoriteLeafKeyDto implements Serializable {

  private static final long serialVersionUID = -443369955123311888L;

  private String treeId;

  private String leafId;

  private String gaId;

  public EshopFavoriteLeafKeyDto(String treeId, String leafId) {
    this.treeId = treeId;
    this.leafId = leafId;
  }
}
