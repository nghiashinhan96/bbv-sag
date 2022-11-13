package com.sagag.services.domain.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleAccessoryDto implements Serializable {

  private static final long serialVersionUID = -237369396748199278L;

  @JsonProperty("seqNo")
  private Integer seqNo;
  
  @JsonProperty("linkType")
  private Integer linkType;

  @JsonProperty("linkVal")
  private String linkVal;

  @JsonProperty("accesoryListsText")
  private String accesoryListsText;
  
  @JsonIgnore()
  private String accesoryLinkText;

  @JsonProperty("accesoryListItems")
  private List<ArticleAccessoryItemDto> accesoryListItems;
  
}
