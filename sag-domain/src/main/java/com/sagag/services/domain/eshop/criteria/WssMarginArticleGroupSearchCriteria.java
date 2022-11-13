package com.sagag.services.domain.eshop.criteria;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WssMarginArticleGroupSearchCriteria implements Serializable {

  private static final long serialVersionUID = 417252100096269293L;

  private String sagArticleGroup;

  private String sagArticleGroupDesc;

  private String customArticleGroup;

  private String customArticleGroupDesc;

  @JsonIgnore
  private Integer orgId;
}
