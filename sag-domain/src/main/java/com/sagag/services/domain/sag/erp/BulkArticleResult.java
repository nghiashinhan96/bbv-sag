package com.sagag.services.domain.sag.erp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "article", "umarId", "articleId", "message", "status" })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkArticleResult implements Serializable {
  private static final long serialVersionUID = 7730964825321219428L;

  private Article article;

  @JsonSerialize(using = ToStringSerializer.class)
  private Long umarId;

  @JsonSerialize(using = ToStringSerializer.class)
  private String articleId;

  private String message;

  private String status;

}
