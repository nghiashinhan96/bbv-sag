package com.sagag.services.ivds.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sagag.services.elasticsearch.dto.IArticleResponseSupport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "total_elements", "view_articles", "aggregations" })
public class CustomArticleResponseDto implements Serializable {

  private static final long serialVersionUID = -5291389747318218150L;

  @JsonProperty("total_elements")
  private long totalElements;

  private Map<String, List<Object>> aggregations;

  @JsonProperty("view_articles")
  private boolean viewArticles;

  public CustomArticleResponseDto(IArticleResponseSupport info) {
    this.totalElements = info.totalArticles();
    this.viewArticles = info.allowViewArticles();
    this.aggregations = info.aggregations();
  }

}
