package com.sagag.services.domain.eshop.unitree.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@JsonPropertyOrder({ "nodeName", "treeId", "parentId", "nodeId", "activeLink" })
public class UnitreeFreetextSearchDto implements Serializable {
  
  private static final long serialVersionUID = 6505946064069401542L;

  private String nodeName;
  
  private String treeId;
  
  private String parentId;

  private String nodeId;
  
  private boolean activeLink;
}
