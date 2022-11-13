package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganisationDto implements Serializable {

  private static final long serialVersionUID = -4073921622275251957L;

  private int id;
  
  private String description;

  private String name;

  private String orgCode;

  private int orderSettingsId;

  private int orgTypeId;

  private int parentId;

  private String settingsId;

  private String shortname;

}
