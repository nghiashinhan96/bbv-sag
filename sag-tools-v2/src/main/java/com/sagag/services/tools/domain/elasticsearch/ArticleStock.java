package com.sagag.services.tools.domain.elasticsearch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"articleId",
"stock",
"status",
"message"
})
public class ArticleStock implements Serializable {

  private static final long serialVersionUID = 3184135837940679155L;

  @JsonSerialize(using = ToStringSerializer.class)
  private Long articleId;

  private Integer stock;

  private String status;

  private String message;

}
