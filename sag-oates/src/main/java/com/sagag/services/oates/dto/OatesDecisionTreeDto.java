package com.sagag.services.oates.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.domain.article.oil.OilTypeIdsDto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
public class OatesDecisionTreeDto extends OilTypeIdsDto implements Serializable {

  private static final long serialVersionUID = -5501119192375753512L;

  private String type;

  @JsonProperty("application_id")
  private String applicationId;

  private String guid;

  private String cateId;

  private String cateName;

  private String displayName;

  private OatesChoiceDto choice;

}
