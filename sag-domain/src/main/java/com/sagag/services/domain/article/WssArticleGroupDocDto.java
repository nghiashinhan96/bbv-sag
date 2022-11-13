package com.sagag.services.domain.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Id;

@Data
@Builder
@JsonInclude(JsonInclude.Include.ALWAYS)
public class WssArticleGroupDocDto implements Serializable {

  private static final long serialVersionUID = 7117050960776389391L;

  @Id
  @JsonProperty("id")
  private String id;

  @JsonProperty("artgrp_tree")
  private List<WssArtGrpTreeDto> wssArtGrpTree;

  @JsonProperty("designations")
  private List<WssDesignationsDto> wssDesignations;
}
