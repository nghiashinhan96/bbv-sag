package com.sagag.services.oates.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OatesApplicationEntity implements Serializable {

  private static final long serialVersionUID = -980058421870330643L;

  @JsonProperty("@id")
  private String id;

  @JsonProperty("@guid")
  private String guid;

  private String name;

  @JsonProperty("app_type")
  private String appType;

  @JsonProperty("app_type_original")
  private String appTypeOriginal;

  @JsonProperty("mixratio")
  private String mixratio;

  @JsonProperty("display_name")
  private String displayName;

  @JsonProperty("display_coption")
  private String displayCoption;

  @JsonProperty("display_lube_note_refs")
  private String displayLubeNoteRefs;

  @JsonProperty("display_capacity")
  private String displayCapacity;

  @JsonProperty("product")
  private List<OatesProduct> products;

  @JsonProperty("coption")
  private OatesCoption coption;

  @JsonProperty("note_ref")
  private List<OatesNoteRef> noteRefs;

  @JsonProperty("change_intervals")
  private List<OatesChangeInterval> changeIntervals;
}
