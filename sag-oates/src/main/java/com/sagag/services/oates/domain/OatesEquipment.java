package com.sagag.services.oates.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OatesEquipment implements Serializable{

  private static final long serialVersionUID = -5759149732460119351L;

  @JsonProperty("@guid")
  private String guid;

  @JsonProperty("@href")
  private String href;

  @JsonProperty("application")
  private List<OatesApplicationEntity> applications;

  @JsonProperty("application_index")
  private OatesApplicationIndex applicationIndex;

  @JsonProperty("app_note")
  private List<OatesAppNote> appNote;

  @JsonProperty("change_intervals")
  private List<OatesChangeInterval> changeIntervals;
}
