package com.sagag.services.domain.eshop.unitree.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({ "treeId", "treeName", "treeImage", "treeSort", "treeExternalServiceAttribute",
    "treeExternalService" })
public class UnitreeCompactDto implements Serializable {

  private static final long serialVersionUID = 3769209863917057963L;

  private String treeId;

  private String treeName;

  private String treeImage;

  private String treeSort;

  private String treeExternalServiceAttribute;

  private String treeExternalService;

}
