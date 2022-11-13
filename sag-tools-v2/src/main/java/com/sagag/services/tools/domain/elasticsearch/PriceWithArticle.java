package com.sagag.services.tools.domain.elasticsearch;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;

import java.io.Serializable;

@Data
public class PriceWithArticle implements Serializable {

  private static final long serialVersionUID = 8595011431758237193L;

  @JsonSerialize(using = ToStringSerializer.class)
  private Long articleId;

  @JsonSerialize(using = ToStringSerializer.class)
  private Long umarId;
  private PriceWithArticlePrice price;

}
