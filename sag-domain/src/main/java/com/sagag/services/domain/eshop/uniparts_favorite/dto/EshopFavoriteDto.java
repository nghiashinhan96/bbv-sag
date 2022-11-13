package com.sagag.services.domain.eshop.uniparts_favorite.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "id", "userId", "title", "comment", "type","vinId", "vehicleId", "articleId",
  "treeId", "leafId", "gaId", "lastUpdate", "createdTime", "addItem" })
public class EshopFavoriteDto implements Serializable {

  private static final long serialVersionUID = -8256474524748002823L;

  private String id;

  private Long userId;

  private String title;

  private String comment;

  private String type;

  private String vinId;

  private String vehicleId;

  private String articleId;

  private String treeId;

  private String leafId;

  private String gaId;

  private Date createdTime;

  private Date lastUpdate;

  private boolean addItem;
}
